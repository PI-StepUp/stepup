import {atom} from "recoil"
import { recoilPersist } from "recoil-persist";

const sessionStorage = typeof window !== 'undefined' ? window.sessionStorage : undefined;
const {persistAtom} = recoilPersist({
    key: 'recoil-state',
    storage: sessionStorage,
});

export const LanguageState = atom({
    key: 'languageState',
    default: 'ko',
    effects_UNSTABLE: [persistAtom]
});

export const accessTokenState = atom({
    key: 'accessTokenState',
    default: '',
    effects_UNSTABLE: [persistAtom]
})

export const refreshTokenState = atom({
    key: 'refreshTokenState',
    default: '',
    effects_UNSTABLE: [persistAtom]
})

export const idState = atom({
    key: 'idState',
    default: '',
    effects_UNSTABLE: [persistAtom]
})

export const nicknameState = atom({
    key: 'nicknameState',
    default: '',
    effects_UNSTABLE: [persistAtom]
})

export const profileImgState = atom({
    key: 'profileImgState',
    default: '',
    effects_UNSTABLE: [persistAtom]
})

export const rankNameState = atom({
    key: 'rankNameState',
    default: '',
    effects_UNSTABLE: [persistAtom]
})

export const roleState = atom({
    key: 'roleState',
    default: '',
    effects_UNSTABLE: [persistAtom]
})