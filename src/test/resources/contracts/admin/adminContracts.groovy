package contracts.admin

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        name "should create a new admin"
        request {
            method 'POST'
            url '/api/admins'
            headers {
                contentType applicationJson()
            }
            body([
                name: "John Doe",
                email: "john@example.com",
                age: 30
            ])
        }
        response {
            status CREATED()
            headers {
                contentType applicationJson()
            }
            body([
                id: anyNumber(),
                name: "John Doe",
                email: "john@example.com",
                age: 30
            ])
        }
    },

    Contract.make {
        name "should get admin by id"
        request {
            method 'GET'
            url '/api/admins/1'
        }
        response {
            status OK()
            headers {
                contentType applicationJson()
            }
            body([
                id: 1,
                name: "John Doe",
                email: "john@example.com",
                age: 30
            ])
        }
    },

    Contract.make {
        name "should get all admins"
        request {
            method 'GET'
            url '/api/admins'
        }
        response {
            status OK()
            headers {
                contentType applicationJson()
            }
            body([
                [
                    id: 1,
                    name: "John Doe",
                    email: "john@example.com",
                    age: 30
                ],
                [
                    id: 2,
                    name: "Jane Doe",
                    email: "jane@example.com",
                    age: 25
                ]
            ])
        }
    },

    Contract.make {
        name "should update admin"
        request {
            method 'PUT'
            url '/api/admins/1'
            headers {
                contentType applicationJson()
            }
            body([
                name: "Updated Name",
                email: "updated@example.com",
                age: 31
            ])
        }
        response {
            status OK()
            headers {
                contentType applicationJson()
            }
            body([
                id: 1,
                name: "Updated Name",
                email: "updated@example.com",
                age: 31
            ])
        }
    },

    Contract.make {
        name "should delete admin"
        request {
            method 'DELETE'
            url '/api/admins/1'
        }
        response {
            status NO_CONTENT()
        }
    },

    Contract.make {
        name "should return 404 when admin not found"
        request {
            method 'GET'
            url '/api/admins/999'
        }
        response {
            status NOT_FOUND()
        }
    },

    Contract.make {
        name "should return 409 when creating admin with duplicate email"
        request {
            method 'POST'
            url '/api/admins'
            headers {
                contentType applicationJson()
            }
            body([
                name: "John Doe 2",
                email: "john@example.com",
                age: 35
            ])
        }
        response {
            status CONFLICT()
        }
    },

    Contract.make {
        name "should return 400 when creating admin with invalid input"
        request {
            method 'POST'
            url '/api/admins'
            headers {
                contentType applicationJson()
            }
            body([
                name: "",
                email: "invalid-email",
                age: -1
            ])
        }
        response {
            status BAD_REQUEST()
        }
    }
]