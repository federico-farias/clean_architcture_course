package com.example.demo.users.domain;

import lombok.AllArgsConstructor;

// Servicio de dominio: Componente que encapsula lógica de negocio
// que no pertenece a una entidad o agregado específico,
// pero que es esencial para el dominio.
// Se utiliza para operaciones que involucran múltiples entidades o agregados,
// o para lógica de negocio que no encaja claramente en una entidad o agregado
// o bien es lógica de negocio que requiere de interacción
// con el mundo exterior (bases de datos, servicios externos, etc.)
@AllArgsConstructor
public class UserFinder {

    private final UserRepository userRepository;

    public User find(UserId userId) {
        User user = this.userRepository.find(userId);
        if (user == null) {
            throw new UserNotFoundException("Usuario no encontrado");
        }
        return user;
    }

}
