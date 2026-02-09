# Kata: Order-to-Cash - Refactoring hacia Clean Architecture

## 🎯 Objetivo

Refactorizar una aplicación tradicional N-capas hacia una arquitectura limpia (Hexagonal/Onion) con DDD y Vertical Slicing.

---

## 📋 Estado Inicial (Legacy)

La aplicación actual es un sistema de gestión de pedidos con las siguientes características "problemáticas" desde el punto de vista arquitectónico:

### Problemas a identificar:

1. **Modelos Anémicos**: Las entidades JPA son simples contenedores de datos sin comportamiento
2. **Lógica de Negocio Dispersa**: Las reglas de negocio están en los servicios mezcladas con infraestructura
3. **Acoplamiento a Frameworks**: Todo depende directamente de Spring y JPA
4. **Sin Separación de Capas**: Controladores → Servicios → Repositorios en un solo módulo
5. **DTOs como contratos externos e internos**: Los mismos DTOs se usan en todas las capas

---

## 🏗️ Estructura Actual

```
com.example.demo.ordertocash/
├── controller/
│   ├── CustomerController.java
│   ├── ProductController.java
│   ├── OrderController.java
│   └── InvoiceController.java
├── service/
│   ├── CustomerService.java
│   ├── ProductService.java
│   ├── OrderService.java
│   ├── InvoiceService.java
│   └── StockReservationService.java
├── repository/
│   ├── CustomerRepository.java
│   ├── ProductRepository.java
│   ├── OrderRepository.java
│   ├── OrderItemRepository.java
│   ├── PromoCodeRepository.java
│   ├── InvoiceRepository.java
│   └── StockReservationRepository.java
├── entity/
│   ├── Customer.java
│   ├── Product.java
│   ├── Order.java
│   ├── OrderItem.java
│   ├── PromoCode.java
│   ├── Invoice.java
│   └── StockReservation.java
├── dto/
│   └── (varios DTOs)
└── exception/
    └── OrderToCashExceptionHandler.java
```

---

## 📦 Funcionalidades

### 1. Gestión de Clientes
- Crear cliente con email, nombre, región y tier
- Regiones válidas: US, EU, LATAM, ASIA
- Tiers: STANDARD, PREMIUM, VIP
- No se puede bajar de tier

### 2. Gestión de Productos
- CRUD de productos con SKU único
- Gestión de stock
- Activar/desactivar productos

### 3. Gestión de Pedidos
- Crear pedido con múltiples items
- Validar stock disponible
- **Reserva de stock** con timeout de 15 minutos
- Aplicar códigos promocionales
- **Descuentos por volumen**:
  - 10-19 unidades: 5%
  - 20-49 unidades: 10%
  - 50+ unidades: 15%
- **Descuentos por tier**:
  - STANDARD: 0%
  - PREMIUM: 5%
  - VIP: 10%
- **Impuestos por región**:
  - US: 8%
  - EU: 21%
  - LATAM: 16%
  - ASIA: 10%

### 4. Flujo de Estados del Pedido
```
PENDING → CONFIRMED → SHIPPED → DELIVERED
    ↓         ↓
CANCELLED  CANCELLED
```

### 5. Facturación
- Generar factura solo para pedidos confirmados+
- Marcar factura como pagada

---

## 🎯 Objetivos del Refactoring

### Fase 1: Identificar Bounded Contexts
- **Orders**: Pedidos, items, estados
- **Customers**: Clientes, tiers, regiones
- **Inventory**: Productos, stock, reservas
- **Billing**: Facturas, pagos

### Fase 2: Extraer Dominio a `demo-application`
1. Crear Value Objects:
   - `Email`, `CustomerTier`, `Region`
   - `Money`, `Quantity`, `SKU`
   - `OrderStatus`, `InvoiceNumber`

2. Crear Entidades de Dominio con comportamiento:
   - `Customer` con reglas de upgrade de tier
   - `Order` con máquina de estados
   - `Product` con validaciones de stock

3. Crear Aggregates:
   - `Order` como aggregate root con `OrderItems`

### Fase 3: Definir Puertos (Interfaces)
- **Puertos de Entrada** (Use Cases):
  - `CreateOrderUseCase`
  - `ConfirmOrderUseCase`
  - `GenerateInvoiceUseCase`
  - etc.

- **Puertos de Salida** (Repository Interfaces):
  - `OrderRepository` (interfaz en dominio)
  - `CustomerRepository` (interfaz en dominio)
  - `ProductRepository` (interfaz en dominio)

### Fase 4: Implementar Adaptadores en `demo-spring-boot`
- Adaptadores de entrada: Controllers REST
- Adaptadores de salida: Implementaciones JPA

---

## 🧪 Criterios de Aceptación

- [ ] El módulo `demo-application` NO tiene dependencias de Spring
- [ ] Las entidades de dominio tienen comportamiento (no son anémicas)
- [ ] Cada caso de uso tiene su propia clase
- [ ] Los Value Objects validan sus invariantes en construcción
- [ ] Los repositorios son interfaces en el dominio
- [ ] La infraestructura puede ser reemplazada sin tocar el dominio
- [ ] Los tests de dominio no requieren Spring

---

## 🚀 Endpoints Disponibles

### Customers
```
POST   /api/ordertocash/customers
GET    /api/ordertocash/customers
GET    /api/ordertocash/customers/{id}
PATCH  /api/ordertocash/customers/{id}/upgrade-tier?tier=PREMIUM
```

### Products
```
POST   /api/ordertocash/products
GET    /api/ordertocash/products
GET    /api/ordertocash/products/{id}
PATCH  /api/ordertocash/products/{id}/stock?quantity=10
GET    /api/ordertocash/products/{id}/available-stock
DELETE /api/ordertocash/products/{id}
```

### Orders
```
POST   /api/ordertocash/orders
GET    /api/ordertocash/orders/{id}
GET    /api/ordertocash/orders/customer/{customerId}
POST   /api/ordertocash/orders/{id}/confirm
PATCH  /api/ordertocash/orders/{id}/status
POST   /api/ordertocash/orders/{id}/cancel
```

### Invoices
```
POST   /api/ordertocash/invoices/order/{orderId}
GET    /api/ordertocash/invoices/{id}
GET    /api/ordertocash/invoices/order/{orderId}
POST   /api/ordertocash/invoices/{id}/pay
```

---

## 📝 Ejemplos de Requests

### Crear Cliente
```json
POST /api/ordertocash/customers
{
    "email": "juan@example.com",
    "name": "Juan Pérez",
    "region": "LATAM",
    "tier": "STANDARD"
}
```

### Crear Producto
```json
POST /api/ordertocash/products
{
    "sku": "PROD-001",
    "name": "Laptop HP",
    "description": "Laptop HP 15 pulgadas",
    "price": 999.99,
    "stockQuantity": 50
}
```

### Crear Pedido
```json
POST /api/ordertocash/orders
{
    "customerId": 1,
    "items": [
        { "productId": 1, "quantity": 2 },
        { "productId": 2, "quantity": 5 }
    ],
    "promoCode": "SUMMER2024"
}
```

---

## 💡 Tips para el Refactoring

1. **Empezar por los Value Objects** - Son los más fáciles y dan victorias rápidas
2. **Usar TDD** - Escribir tests antes de refactorizar
3. **Pequeños pasos** - Refactorizar de a poco, manteniendo siempre verde
4. **Strangler Fig Pattern** - La nueva arquitectura puede coexistir con la legacy
5. **No romper los endpoints** - Los contratos HTTP deben mantenerse

