/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo.users.porin;

import com.example.demo.users.application.UserRegistrationCommand;
import com.example.demo.users.application.UserRegistrationResponse;
import com.example.demo.users.application.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

@Controller("/hello") // <1>
public class HelloController {

    private UserService userService;

    public HelloController(UserService userService) {
        this.userService = userService;
    }

    @Post // <2>
    @Produces(MediaType.APPLICATION_JSON) // <3>
    public HttpResponse<?> index(@Body UserRegistrationDto registrationDto) {
        try {
            UserRegistrationCommand command = new UserRegistrationCommand(
                    registrationDto.getUsername(),
                    registrationDto.getEmail(),
                    registrationDto.getPassword(),
                    registrationDto.getConfirmPassword(),
                    registrationDto.getFirstName(),
                    registrationDto.getLastName()
            );
            UserRegistrationResponse resultCommand = userService.registerUser(command);
            UserResponseDto userResponse = new UserResponseDto(
                    resultCommand.getId(),
                    resultCommand.getUsername(),
                    resultCommand.getEmail(),
                    resultCommand.getFirstName(),
                    resultCommand.getLastName(),
                    resultCommand.getCreatedAt(),
                    resultCommand.isEnabled()
            );
            return HttpResponse.ok(userResponse);
        } catch (IllegalArgumentException e) {
            return HttpResponse.badRequest(new MicronoutHttpErrorResponse(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResponse.serverError(new MicronoutHttpErrorResponse("Error interno del servidor: " + e.getMessage()));
        }
    }

}
