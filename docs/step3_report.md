# Step 3 – API & Error Handling Report

## Our custom API
[FairSHare API](https://github.com/Marten221/FairShareAPI)

## Which API was chosen and why
We use **Retrofit** (with OkHttp). It’s simple, works well with coroutines, and is standard on Android. Base URL: `https://ojasaar.com/fairshareapi/`.

## Example API endpoint used
The `AuthApi` defines two endpoints that accept a JSON body of `{ email, password }`:
- `POST public/login`
- `POST public/register`  
The request model is `AuthRequest(email, password)`.

## Error handling strategy
Errors are centralized in `AuthRepository`:
- All calls go through a `call { ... }` wrapper that returns `Result<String>`.
- **Network issues** (`IOException`: no internet / timeout / DNS) → user‑friendly `"No internet connection or timeout. Please try again."`
- **Unreachable server** (`HttpException`) → `"Server unreachable. Please try again later."`
- **HTTP errors** (non‑2xx) are **humanized**:
  - `401` → `"Incorrect email or password"`
  - Try to read a server JSON error `{"message": "..."};` if present, show that.
  - Fallbacks by code: `400` invalid request, `403` permission, `404` not found, `500/502/503` server error, else unexpected error.
 
  **UI flow on success/failure:** `AuthViewModel` exposes `AuthState`. On success we navigate to **Groups**; on error we show a concise dialog.
