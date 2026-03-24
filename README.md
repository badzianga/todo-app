# ToDo App

REST API for ToDo App written in Java using Spring Boot, Spring Security and JWT authorization.

## Endpoints

- `POST /api/v1/auth/register [1]` - register a new user
- `POST /api/v1/auth/login [1]` - login and get JWT token (string)
- `GET /api/v1/tasks [S][P]` - get tasks of the user
- `GET /api/v1/tasks/{id} [S]` - get user's task with given id
- `POST /api/v1/tasks [S][2]` - add a new task, return it
- `PUT /api/v1/tasks/{id} [S][3]` - update task (title/description/completion)
- `PATCH /api/v1/tasks/{id} [S]` - swap task completion status
- `DELETE /api/v1/tasks/{id} [S]` - delete task with given id

### Legend:

- `[S]` - Endpoint is secured with JWT authorization. Add `Authorization: Bearer <JWT_TOKEN>` to request header
- `[P]` - Pagination and sorting is handled
- `[1]` - [AuthRequest](src/main/java/com/badzianga/todo/request/AuthRequest.java)
- `[2]` - [AddTaskRequest](src/main/java/com/badzianga/todo/request/AddTaskRequest.java)
- `[3]` - [UpdateTaskRequest](src/main/java/com/badzianga/todo/request/UpdateTaskRequest.java)

`[n]` Requests are used as JSONs in request body, e.g. AuthRequest is
{"email": "e@mail.com", "password": "secret-password"}.

All requests returns [ApiResponse](src/main/java/com/badzianga/todo/response/ApiResponse.java), which is a JSON struct with
message and data field. Message field indicates status - e.g. "Success" or error message, data field might contain data
or nothing. When endpoint returns something, it means that it can be found in data field, e.g. token might mean
{"message": "Success", "data": "<JWT_TOKEN_HERE>"}
