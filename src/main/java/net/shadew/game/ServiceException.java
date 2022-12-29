/*
 * Copyright 2022 Shadew
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package net.shadew.game;

public class ServiceException extends GameException {
    private final Service<?> service;

    public ServiceException(Service<?> service) {
        this(service, null, null);
    }

    public ServiceException(Service<?> service, String message) {
        this(service, message, null);
    }

    public ServiceException(Service<?> service, Throwable cause) {
        this(service, null, cause);
    }

    public ServiceException(Service<?> service, String message, Throwable cause) {
        super(message, cause);
        this.service = service;
    }

    public ServiceException(Service<?> service, boolean fatal) {
        this(service, fatal, null, null);
    }

    public ServiceException(Service<?> service, boolean fatal, String message) {
        this(service, fatal, message, null);
    }

    public ServiceException(Service<?> service, boolean fatal, Throwable cause) {
        this(service, fatal, null, cause);
    }

    public ServiceException(Service<?> service, boolean fatal, String message, Throwable cause) {
        super(fatal, message, cause);
        this.service = service;
    }

    public Service<?> service() {
        return service;
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        if (msg != null) return "[in service " + service.id() + "] " + msg;
        return "in service " + service.id();
    }
}
