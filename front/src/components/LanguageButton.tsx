import React, {useState} from "react";

import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";
import Image from "next/image";

import HoverCloseIcon from "/public/images/icon-hover-close.svg"

const LanguageButton = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    const [koreanActive, setKoreanActive] = useState(false);
    const [englishActive, setEnglishActive] = useState(false);
    const [chineseActive, setChineseActive] = useState(false);
    const goTopAnimation = () => {
        if(!koreanActive){
            setKoreanActive(true);
            setEnglishActive(true);
            setChineseActive(true);
        }else if(koreanActive){
            setKoreanActive(false);
            setEnglishActive(false);
            setChineseActive(false);
        }
    }
    return(
        <>
            <div className="language-button-wrap">
                <div className="language-content">
                    <ul>
                        <li className={koreanActive ? "korean-animation" : "korean-hidden-animation"} onClick={() => {
                            setLang('ko')
                        }}>
                            <span>한</span>
                        </li>
                        <li className={englishActive ? "english-animation" : "english-hidden-animation"} onClick={() => {
                            setLang('en')
                        }}>
                            <span>E</span>
                        </li>
                        <li className={chineseActive ? "chinese-animation" : "chinese-hidden-animation"} onClick={() => {
                            setLang('cn')
                        }}>
                            <span>语</span>
                        </li>
                        <li onClick={goTopAnimation}>
                            <span>{koreanActive ? <Image src={HoverCloseIcon} alt=""/> : "language"}</span>
                        </li>
                    </ul>
                </div>
            </div>
        </>
    )
}

export default LanguageButton;