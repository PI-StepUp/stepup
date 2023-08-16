import { useEffect, useState } from "react";
import Link from "next/link";

import { LanguageState, nicknameState, accessTokenState, refreshTokenState, idState, profileImgState, rankNameState, roleState } from "states/states";
import { useRecoilState } from "recoil";
import { useRouter } from "next/router";

const MainSideMenu = (props : any) => {
    console.log(props.state);
    const [lang, setLang] = useRecoilState(LanguageState);
    const [nickname, setNickname] = useRecoilState(nicknameState);
    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);
    const [profileImg, setProfileImg] = useRecoilState(profileImgState);
    const [rankname, setRankname] = useRecoilState(rankNameState);
    const [role, setRole] = useRecoilState(roleState);
    const [nav, setNav] = useState<any>(<ul>
		<li><Link href="/login">{lang === "en" ? "LOGIN" : lang === "cn" ? "登陆" : "로그인"}</Link></li>
		<li><Link href="/signup">{lang === "en" ? "SIGNUP" : lang === "cn" ? "注册会员" : "회원가입"}</Link></li>
	</ul>);

    const router = useRouter();

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
    return(
        <>
            {props.state == "block" ? 
                <nav className="main-side-menu-wrap">
                    <ul>
                        <li><Link href="/randomplay/list">{lang==="en" ? "Random play" : lang==="cn" ? "随机播放" : "랜덤플레이" }</Link></li>
                        <li><Link href="/notice/list">{lang==="en" ? "Community" : lang==="cn" ? "公社" : "커뮤니티" }</Link></li>
                        <li><Link href="/playlist/list">{lang==="en" ? "New song" : lang==="cn" ? "新歌申请" : "신곡신청" }</Link></li>
                        <li><Link href="/practiceroom">{lang==="en" ? "Practice room" : lang==="cn" ? "进入练习室" : "연습실입장" }</Link></li>
                    </ul>
                    <ul>
                        {nav}
                    </ul>
                </nav>
                :
                <></>
            }
        </>
    )
}

export default MainSideMenu;