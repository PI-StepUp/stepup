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