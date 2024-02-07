# OpenAPI definition
## Version: v0

### /api/v1/words/{id}

#### PUT
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path |  | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/word-set/{id}

#### GET
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path |  | Yes | string |
| page | query |  | No | integer |
| size | query |  | No | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

#### PUT
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path |  | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/users/current/update

#### PUT
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/words/word-set/{id}

#### POST
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path |  | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/word-set

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/word-set/bind

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/records/create

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/quiz/generate/vocabulary

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/quiz/generate/txt

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/quiz/generate/txt-to-vocab

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/quiz/generate/text

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/quiz/generate/text-to-vocab

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/quiz/generate/pdf

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/quiz/generate/pdf-to-vocab

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/quiz/generate/doc

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/quiz/generate/doc-to-vocab

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/auth/verify

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/auth/send-reset-password

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/auth/reset-password

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/auth/register

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/auth/refresh-token

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/auth/login

#### POST
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/users/current

#### GET
##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/users/current/word-set

#### GET
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| page | query |  | No | integer |
| size | query |  | No | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/users/current/records

#### GET
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| page | query |  | No | integer |
| size | query |  | No | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/users/current/record

#### GET
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | query |  | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/users/current/quizzes

#### GET
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| page | query |  | No | integer |
| size | query |  | No | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/users/current/quiz

#### GET
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | query |  | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/records/{id}

#### GET
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path |  | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/images/{id}

#### GET
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path |  | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/auth/send-verify

#### GET
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| username | query |  | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/word-set/{id}/delete

#### DELETE
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path |  | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### /api/v1/quiz/{id}/delete

#### DELETE
##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path |  | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
