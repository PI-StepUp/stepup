import axios from "axios";

const SERVER_URL = "https://stepup-pi.com:8080/api";

export const axiosUser = axios.create({
    baseURL: SERVER_URL + "/user"
})

export const axiosBoard = axios.create({
    baseURL: SERVER_URL + "/board"
})

export const axiosMusic = axios.create({
    baseURL: SERVER_URL + "/music"
})

export const axiosDance = axios.create({
    baseURL: SERVER_URL + "/dance"
})

export const axiosRank = axios.create({
    baseURL: SERVER_URL + "/rank"
})