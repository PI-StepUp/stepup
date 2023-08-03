import {atom} from "recoil"

export const LanguageState = atom({
    key: 'languageState',
    default: 'ko',
});

export const accessTokenState = atom({
    key: 'accessTokenState',
    default: '',
})

export const refreshTokenState = atom({
    key: 'refreshTokenState',
    default: '',
})

export const idState = atom({
    key: 'idState',
    default: '',
})

export const nicknameState = atom({
    key: 'nicknameState',
    default: '',
})

export const profileImgState = atom({
    key: 'profileImgState',
    default: '',
})

export const rankNameState = atom({
    key: 'rankNameState',
    default: '',
})