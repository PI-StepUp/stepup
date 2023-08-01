import React,{useRef} from "react";
import Image from "next/image";

import KoreanIcon from "/public/images/icon-korean.svg"
import EnglishIcon from "/public/images/icon-english.svg"
import ChineseIcon from "/public/images/icon-chinese.svg"
import LanguageIcon from "/public/images/icon-language.svg"

import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

const LanguageButton = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    return(
        <>
            <div className="language-button-wrap">
                <div className="language-content">
                    <ul>
                        <li onClick={() => {
                            setLang('ko')
                        }}>
                            <span>한국어</span>
                            <div className="language-img">
                                <Image src={KoreanIcon} alt=""/>
                            </div>
                        </li>
                        <li onClick={() => {
                            setLang('en')
                        }}>
                            <span>English</span>
                            <div className="language-img">
                                <Image src={EnglishIcon} alt=""/>
                            </div>
                        </li>
                        <li onClick={() => {
                            setLang('cn')
                        }}>
                            <span>汉语</span>
                            <div className="language-img">
                                <Image src={ChineseIcon} alt=""/>
                            </div>
                        </li>
                        <li>
                            <span>언어선택</span>
                            <div className="language-img">
                                <Image src={LanguageIcon} alt=""/>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </>
    )
}

export default LanguageButton;