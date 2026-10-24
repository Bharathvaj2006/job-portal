
import axios from "axios";
import { auth } from "../utils/auth";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use(
  (config) => {
    const token = auth.getToken();

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    const requestUrl = error.config?.url ?? "";

    const isAuthEndpoint =
      /\/api\/auth\/(login|register)(?:[?#]|$)/.test(requestUrl);

    if (status === 401 && !isAuthEndpoint) {
      auth.removeToken();

      // Redirect only when the user is not already on the login page.
      if (window.location.pathname !== "/login") {
        window.location.assign("/login");
      }
    }

    if (!error.response) {
      error.userMessage = error.request
        ? "Unable to connect to the server. Check your connection and try again."
        : "The request could not be prepared. Please try again.";
    } else {
      const messages = {
        400: "The request contains invalid data.",
        401: "Your session has expired. Please log in again.",
        403: "You do not have permission to perform this action.",
        404: "The requested resource was not found.",
        409: "This action conflicts with existing data.",
        413: "The uploaded file is too large.",
        415: "This file format is not supported.",
        422: "The submitted data could not be processed.",
        429: "Too many requests. Please wait and try again.",
      };

      error.userMessage =
        messages[status] ??
        (status >= 500
          ? "A server error occurred. Please try again later."
          : "Something went wrong. Please try again.");
    }

    return Promise.reject(error);
  }
);

export default api;
