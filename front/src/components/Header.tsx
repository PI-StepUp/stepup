import React from "react"
import Link from "next/link"

const Header = () => {
    return (
        <>
            <header className="header">
                <div className="block-margin">
                    <div className="logo">
                        <h1><Link href="/">STEP UP</Link></h1>
                    </div>
                    <nav>
                        <ul>
                            <li><h2><Link href="/">랜덤플레이</Link></h2></li>
                            <li><h2><Link href="/notice/list">커뮤니티</Link></h2></li>
                            <li><h2><Link href="/playlist/list">신곡신청</Link></h2></li>
                            <li><h2><Link href="/">연습실참가</Link></h2></li>
                        </ul>
                    </nav>
                    <div className="login-wrap">
                        <ul>
                            <li><Link href="/login">로그인</Link></li>
                            <li><Link href="/signup">회원가입</Link></li>
                        </ul>
                    </div>
                </div>
            </header>

        </>
    )
}

export default Header;