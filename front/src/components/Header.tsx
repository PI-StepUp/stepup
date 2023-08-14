import React, {useEffect, useState} from "react"
import Link from "next/link"
import Image from "next/image"

import { useRecoilState } from "recoil";
import { LanguageState, nicknameState, accessTokenState, refreshTokenState, idState, profileImgState, rankNameState, roleState } from "states/states";
import { useRouter } from "next/router";

import hamburgerMenu from "/public/images/hamburger-menu-white.svg"

import MainSideMenu from "./MainSideMenu";

const Header = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    const [nickname, setNickname] = useRecoilState(nicknameState);
    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);
    const [profileImg, setProfileImg] = useRecoilState(profileImgState);
    const [rankname, setRankname] = useRecoilState(rankNameState);
    const [role, setRole] = useRecoilState(roleState);
	const [sideMenu, setSideMenu] = useState<Boolean>(false);
    const [nav, setNav] = useState<any>(<ul>
		<li><Link href="/login">{lang === "en" ? "LOGIN" : lang === "cn" ? "登陆" : "로그인"}</Link></li>
		<li><Link href="/signup">{lang === "en" ? "SIGNUP" : lang === "cn" ? "注册会员" : "회원가입"}</Link></li>
	</ul>);

    const router = useRouter();

    const openSideMenu = () => {
		switch(sideMenu){
			case true:
				setSideMenu(false);
				break;
			case false:
				setSideMenu(true);
				break;
		}
	}

    const signout = () => {
        alert("로그아웃 되었습니다.");
        setAccessToken("");
        setRefreshToken("");
        setNickname("");
        setId("");
        setProfileImg("");
        setRankname("");
        setRole("");
        router.push('/');
    }

    const movePracticeRoom = () => {
		if(nickname == ""){
			alert("해당 서비스는 로그인 후 이용하실 수 있습니다.");
		}else{
			router.push({
				pathname: "/practiceroom",
				query:{
					token: accessToken,
				}
			});
		}
	}

    useEffect(() => {
		if(nickname != ""){
			setNav(<ul>
				<li onClick={signout}>{lang === "en" ? "SIGNOUT" : lang === "cn" ? "注销" : "로그아웃"}</li>
				<li><Link href="/mypage">{lang === "en" ? "Mypage" : lang === "cn" ? "我的页面" : "마이페이지"}</Link></li>
			</ul>);
		}else if(nickname == ""){
			setNav(
				<ul>
					<li><Link href="/login">{lang === "en" ? "LOGIN" : lang === "cn" ? "登陆" : "로그인"}</Link></li>
					<li><Link href="/signup">{lang === "en" ? "SIGNUP" : lang === "cn" ? "注册会员" : "회원가입"}</Link></li>
				</ul>
			);
		}
	}, [])

    return (
        <>
            <header className="header">
                <div className="block-margin">
                    <div className="logo">
                        <h1>
							<Link href="/">STEP UP</Link>
						</h1>
						<div className="hamburger-menu" onClick={openSideMenu}>
							<Image src={hamburgerMenu} alt=""/>
						</div>
                    </div>
                    <nav>
                        <ul>
                            <li><h2><Link href="/randomplay/list">{lang==="en" ? "Random play" : lang==="cn" ? "随机播放" : "랜덤플레이" }</Link></h2></li>
                            <li><h2><Link href="/notice/list">{lang==="en" ? "Community" : lang==="cn" ? "公社" : "커뮤니티" }</Link></h2></li>
                            <li><h2><Link href="/playlist/list">{lang==="en" ? "New song" : lang==="cn" ? "新歌申请" : "신곡신청" }</Link></h2></li>
                            <li onClick={movePracticeRoom}><h2>{lang==="en" ? "Practice room" : lang==="cn" ? "进入练习室" : "연습실입장" }</h2></li>
                        </ul>
                    </nav>
                    <div className="login-wrap">
                        {nav}
                    </div>
                </div>
            </header>
            {
				sideMenu ?
				<MainSideMenu state="block"/>
				:
				<MainSideMenu state="none"/>
			}
        </>
    )
}

export default Header;