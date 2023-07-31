import React from "react"
import Link from "next/link"

import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

const Header = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    return (
        <>
            <header className="header">
                <div className="block-margin">
                    <div className="logo">
                        <h1><Link href="/">STEP UP</Link></h1>
                    </div>
                    <nav>
                        <ul>
                            <li><h2><Link href="/randomplay/list">{lang==="en" ? "Random play" : lang==="cn" ? "随机播放" : "랜덤플레이" }</Link></h2></li>
                            <li><h2><Link href="/notice/list">{lang==="en" ? "Community" : lang==="cn" ? "公社" : "커뮤니티" }</Link></h2></li>
                            <li><h2><Link href="/playlist/list">{lang==="en" ? "New song" : lang==="cn" ? "新歌申请" : "신곡신청" }</Link></h2></li>
                            <li><h2><Link href="/practiceroom">{lang==="en" ? "Practice room" : lang==="cn" ? "进入练习室" : "연습실입장" }</Link></h2></li>
                        </ul>
                    </nav>
                    <div className="login-wrap">
                        <ul>
                            <li><Link href="/login">{lang==="en" ? "Login" : lang==="cn" ? "注册" : "로그인" }</Link></li>
                            <li><Link href="/signup">{lang==="en" ? "SignUp" : lang==="cn" ? "注册会员" : "회원가입" }</Link></li>
                        </ul>
                    </div>
                </div>
            </header>

        </>
    )
}

export default Header;