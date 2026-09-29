const TOKEN_KEY = "token";

export const auth = {
  getToken() {
    return sessionStorage.getItem(TOKEN_KEY);
  },

  setToken(token) {
    sessionStorage.setItem(TOKEN_KEY, token);
  },

  removeToken() {
    sessionStorage.removeItem(TOKEN_KEY);
  },

  isAuthenticated() {
    return Boolean(this.getToken());
  },
};
