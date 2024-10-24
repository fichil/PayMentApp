import Cookies from "js-cookie";
const TokenKey = "ecard_token1";//作为cookie的名字来用

export function getToken() {
    return Cookies.get(TokenKey)
}

export function setToken(token) {
    Cookies.set(TokenKey, token)
}

export function removeToken() {
    Cookies.remove(TokenKey);
}
