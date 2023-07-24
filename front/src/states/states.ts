import {atom} from "recoil"

const LanguageState = atom({
    key: 'languageState',
    default: 'ko',
});

export {LanguageState};