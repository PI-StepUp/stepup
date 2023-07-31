import axios from "axios";

const SERVER_URL = "http://localhost:8080/api"

export const axiosUser = axios.create({
    baseURL: SERVER_URL + "/user"
})

export const axiosNotice = axios.create({
    baseURL: SERVER_URL + "/board"
})