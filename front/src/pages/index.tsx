import {useEffect, useState, useRef} from "react"

import Link from "next/link"
import Image from "next/image"
import motionIcons from "/public/images/motion-icon.svg"
import personIcon from "/public/images/person-icon.svg"
import shareScreen from "/public/images/sharescreen-icon.svg"
import rankIcon from "/public/images/rank-icon.svg"
import musicNoteIcon from "/public/images/musicnote-icon.svg"
import realtimeRandomPlay1 from "/public/images/realtimeRandomplayImg1.png"
import logo from "/public/images/stepup-logo.svg"
import hamburgerMenu from "/public/images/hamburger-menu.svg"
import footprint from "/public/images/footprint.svg"

import Footer from "components/Footer"
import LanguageButton from "components/LanguageButton";

import { useRecoilState } from "recoil";
import { LanguageState, nicknameState, accessTokenState, refreshTokenState, idState, profileImgState, rankNameState, roleState } from "states/states";
import { useRouter } from "next/router";
import { useInView } from "react-intersection-observer";
import { useInterval } from "usehooks-ts";
import { axiosDance } from "apis/axios";
import MainSideMenu from "components/MainSideMenu"

const Index = () => {
	const [lang, setLang] = useRecoilState(LanguageState);
	const [nickname, setNickname] = useRecoilState(nicknameState);
	const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);
    const [profileImg, setProfileImg] = useRecoilState(profileImgState);
    const [rankname, setRankname] = useRecoilState(rankNameState);
	const [role, setRole] = useRecoilState(roleState);
	const [mainBanner, setMainBanner] = useState<any>(1);
	const mainSideMenu = useRef<any>();

	const router = useRouter();

	const [mainInfo, inView] = useInView();
	const [rooms, setRooms] = useState<any>();
	const [nav, setNav] = useState<any>(<ul>
		<li><Link href="/login">{lang === "en" ? "LOGIN" : lang === "cn" ? "登陆" : "로그인"}</Link></li>
		<li><Link href="/signup">{lang === "en" ? "SIGNUP" : lang === "cn" ? "注册会员" : "회원가입"}</Link></li>
	</ul>);
	const [sideMenu, setSideMenu] = useState<Boolean>(false);

	useInterval(() => {
		if(mainBanner === 1){
			setMainBanner(2);
		}else if(mainBanner === 2){
			setMainBanner(1);
		}
	}, 6000)

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
		setNav(<ul>
			<li><Link href="/login">{lang === "en" ? "LOGIN" : lang === "cn" ? "登陆" : "로그인"}</Link></li>
			<li><Link href="/signup">{lang === "en" ? "SIGNUP" : lang === "cn" ? "注册会员" : "회원가입"}</Link></li>
		</ul>);
		router.push("/");
    }

	const movePracticeRoom = () => {
		if(nickname == ""){
			alert("해당 서비스는 로그인 후 이용하실 수 있습니다.");
		}else{
			router.push({
				pathname: "/practiceroom",
			});
		}
	}

	useEffect(() => {
		axiosDance.get('',{
            params: {
                progressType: "ALL",
            },
        }).then((data) => {
            console.log(data);
            if(data.data.message === "참여 가능한 랜덤 플레이 댄스 목록 조회 완료"){
                setRooms(data.data.data);
            }
        })
	}, [])

	useEffect(() => {
		if (nickname !== "") {
			setNav(
				<ul>
					<li onClick={signout}>
						{lang === "en" ? "SIGNOUT" : lang === "cn" ? "注销" : "로그아웃"}
					</li>
					<li>
						<Link href="/mypage">
							{lang === "en" ? "Mypage" : lang === "cn" ? "我的页面" : "마이페이지"}
						</Link>
					</li>
				</ul>
			);
		} else if (nickname === "") {
			setNav(
				<ul>
					<li>
						<Link href="/login">
							{lang === "en" ? "LOGIN" : lang === "cn" ? "登陆" : "로그인"}
						</Link>
					</li>
					<li>
						<Link href="/signup">
							{lang === "en" ? "SIGNUP" : lang === "cn" ? "注册会员" : "회원가입"}
						</Link>
					</li>
				</ul>
			);
		}
	}, [inView, lang, nickname]);
	
	return (
		<>
			<header className="main-header">
				<div className="block-margin">
					<div className="logo">
						<h1>
							<Link href="/">
								<Image src={logo} alt=""></Image>
								<div className="logo-info">
									<span>STEP UP</span>
									<p>RANDOM PLAY DANCE</p>
								</div>
								<Image src={footprint} alt="" className="left-footprint"></Image>
								<Image src={footprint} alt="" className="right-footprint"></Image>
							</Link>
						</h1>
						<div className="hamburger-menu" onClick={openSideMenu}>
							<Image src={hamburgerMenu} alt=""/>
						</div>
					</div>
					<nav>
						<ul>
							<li><h2><Link href="/randomplay/list">{lang === "en" ? "RANDOMPLAY" : lang === "cn" ? "随机播放" : "랜덤플레이"}</Link></h2></li>
							<li><h2><Link href="/notice/list">{lang === "en" ? "COMMUNITY" : lang === "cn" ? "疏通" : "커뮤니티"}</Link></h2></li>
							<li><h2><Link href="/playlist/list">{lang === "en" ? "NEW SONG" : lang === "cn" ? "新歌申请" : "신곡신청"}</Link></h2></li>
							<li onClick={movePracticeRoom}><h2>{lang === "en" ? "PRACTICE ROOM" : lang === "cn" ? "舞蹈练习室" : "연습실입장"}</h2></li>
						</ul>
					</nav>
					<div className="login-wrap">
						{nav}
					</div>
				</div>
			</header>
			<div className="main-banner">
				{
					mainBanner === 1 ?
					<div className={`main-banner-image visible`} style={{ backgroundImage: `url("/images/main-mainbanner.png")` }}></div>
					:
					<div className={`main-banner-image visible`} style={{ backgroundImage: `url("/images/main-mainbanner2.jpg")` }}></div>

				}
			</div>
			<div className="main-info" ref={mainInfo}>
				{inView ? 
					<div className="flex-wrap" style={{animationName: "top-animation"}}>
						<h3>
							{lang === "en" ? "What KPOP needs" : lang === "cn" ? "KPOP所需的" : "KPOP에 필요한"}<br />
							{lang === "en" ? "Every encounter" : lang === "cn" ? "所有的相遇" : "모든 만남이"}<br />
							{lang === "en" ? "where you are" : lang === "cn" ? "所在地方" : "있는 곳"}
						</h3>
						<div className="main-info-text">
							<p>
								{lang === "en" ? "Dance that you want regardless of the place, anytime, anywhere," : lang === "cn" ? "无论何时何地，不受场所限制，跳自己喜欢的舞蹈，" : "언제, 어디서든 장소에 구애받지 않고 원하는 춤을,"}<br />
								{lang === "en" ? "Everyone can enjoy it in one screen." : lang === "cn" ? "大家可以在一个画面中享受。" : "모두가 한 화면속에서 즐길 수 있어요."}
							</p>
							<p>
								{lang === "en" ? "A lanfle dan stage is provided according to your time." : lang === "cn" ? "根据您的时间，提供随机跳舞的舞台。" : "당신의 시간에 맞게 랜플댄 무대가 제공됩니다."}
							</p>
						</div>
					</div>
					:
					<div className="flex-wrap">
						<h3>
							{lang === "en" ? "What KPOP needs" : lang === "cn" ? "KPOP所需的" : "KPOP에 필요한"}<br />
							{lang === "en" ? "Every encounter" : lang === "cn" ? "所有的相遇" : "모든 만남이"}<br />
							{lang === "en" ? "where you are" : lang === "cn" ? "所在地方" : "있는 곳"}
						</h3>
						<div className="main-info-text">
							<p>
								{lang === "en" ? "Dance that you want regardless of the place, anytime, anywhere," : lang === "cn" ? "无论何时何地，不受场所限制，跳自己喜欢的舞蹈，" : "언제, 어디서든 장소에 구애받지 않고 원하는 춤을,"}<br />
								{lang === "en" ? "Everyone can enjoy it in one screen." : lang === "cn" ? "大家可以在一个画面中享受。" : "모두가 한 화면속에서 즐길 수 있어요."}
							</p>
							<p>
								{lang === "en" ? "A lanfle dan stage is provided according to your time." : lang === "cn" ? "根据您的时间，提供随机跳舞的舞台。" : "당신의 시간에 맞게 랜플댄 무대가 제공됩니다."}
							</p>
						</div>
					</div>
				}
				<div className="main-info-icon-wrap">
					<ul>
						<li>
							<Image src={motionIcons} alt=""></Image>
							<span>{lang === "en" ? "motion recognition" : lang === "cn" ? "动作识别" : "춤 모션인식"}</span>
						</li>
						<li>
							<Image src={personIcon} alt=""></Image>
							<span>{lang === "en" ? "a large-scale" : lang === "cn" ? "大规模" : "대규모 랜플댄"}</span>
						</li>
						<li>
							<Image src={shareScreen} alt=""></Image>
							<span>{lang === "en" ? "screen sharing" : lang === "cn" ? "屏幕共享" : "화면공유"}</span>
						</li>
						<li>
							<Image src={musicNoteIcon} alt=""></Image>
							<span>{lang === "en" ? "sound source sync" : lang === "cn" ? "音源同步" : "음원싱크"}</span>
						</li>
						<li>
							<Image src={rankIcon} alt=""></Image>
							<span>{lang === "en" ? "ranking calculation" : lang === "cn" ? "排名计算" : "순위산출"}</span>
						</li>
					</ul>
				</div>
			</div>
			<div className="ad-randomplay-wrap">
				<div className="ad-randomplay-info">
					<div className="ad-randomplay-info-wrap">
						<h3>
							{lang === "en" ? "Whenever, wherever, random KPOP" : lang === "cn" ? "无论何时何地随机的KPOP" : "언제, 어디서든 랜덤한"}
							{lang === "en" ? " a place that comes out together" : lang === "cn" ? " 一块儿出来的地方" : " KPOP이 함께 나오는 곳"}
						</h3>
						<p>
							{lang === "en" ? "You can dance wherever you want, anytime A lanfle dan stage is provided according to your time." : lang === "cn" ? "无论何时何地都可以不受场所限制跳自己喜欢的舞蹈 根据您的时间，提供随机跳舞的舞台。" : "언제, 어디서든 장소에 구애받지 않고 원하는 춤을 출 수 있어요 당신의 시간에 맞게 랜플댄 무대가 제공됩니다."}
						</p>
						<span><Link href="./randomplay/list">{lang === "en" ? "Participating in Random Play Dance" : lang === "cn" ? "参与随机舞蹈" : "랜덤플레이댄스 참여하기"}</Link></span>
					</div>
				</div>
				<div className="ad-randomplay-img">

                </div>
            </div>
            <div className="realtime-randomplay">
                <div className="realtime-randomplay-title">
                    <h3>{lang==="en" ? "Real-time popular RANDOM PLAY DANCE" : lang==="cn" ? "实时人气随机舞蹈" : "실시간 인기 랜플댄" }</h3>
                    <p>{lang==="en" ? "join the most popular random play dance room right now." : lang==="cn" ? "请参加目前最有人气的随机跳舞房吧" : "현재 가장 인기가 많은 랜덤플레이댄스방에 참여해보세요." }</p>
                </div>
                <div className="realtime-randomplay-content">
                    <ul>
						{rooms?.map((room: any, index: any) => {
							if(index >= 0 && index < 4){
								return(
									<li><Link href={{
											pathname: `/danceroom/${room.randomDanceId}`,
											query:{
												title: room.title,
												content: room.content,
												startAt: room.startAt,
												endAt: room.endAt,
												myName: nickname,
											},
										}}>
										<div className="realtime-randomplay-content-img">
											<Image src={realtimeRandomPlay1} alt=""/>
										</div>
										<div className="realtime-randomplay-content-info">
											<h4>{room.title}</h4>
											<p>관심 26 | 참여 AM10시 ~ PM3시</p>
										</div></Link>
									</li>
								)
							}
						})}
                    </ul>
                </div>
            </div>
            <div className="ad-banner-wrap">
                <div className="ad-banner-title-wrap">
                    <h3>{lang==="en" ? "I want to be a member of STEP UP" : lang==="cn" ? "成为STEP UP的成员" : "STEP UP의 멤버가 되어" }
                        <b className="bold">
                            {lang==="en" ? " Enjoy more Lanfle Dancing" : lang==="cn" ? " 享受更多的随机舞蹈吧" : " 더 많은 랜플댄을 즐겨보세요" }
                        </b>
                    </h3>
                </div>
                <div className="ad-banner-button-wrap">
                    <button><Link href="./signup">{lang==="en" ? "Use after registering as a member" : lang==="cn" ? "注册会员后使用" : "회원가입 후 이용하기" }</Link></button>
                    <button><Link href="./randomplay/list">{lang==="en" ? "Participating in LANPLE DANCING room" : lang==="cn" ? "参与随机舞蹈的房间" : "랜플댄 방 참여하기" }</Link></button>
                </div>
            </div>
            <Footer/>
            <LanguageButton/>
			{
				sideMenu ?
				<MainSideMenu state="block"/>
				:
				<MainSideMenu state="none"/>
			}
        </>
    )
}

export default Index;