import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

import Link from "next/link";
import Image from "next/image";
import logo from "/public/images/stepup-logo.svg";
import footprint from "/public/images/footprint.svg";

const Footer = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    return (
        <>
            <footer>
                <div className="footer-top">
                    <div className="footer-logo">
                        <h5><Link href="/">
                                <Image src={logo} alt=""></Image>
								<div className="logo-info">
                                    <p>KPOP에 필요한 모든 만남</p>
									<span>STEP UP</span>
                                </div>
                                <Image src={footprint} alt="" className="left-footprint"></Image>
								<Image src={footprint} alt="" className="right-footprint"></Image>
                            </Link></h5>
                    </div>
                    <div className="footer-nav">
                        <ul>
                            <li>{lang==="en" ? "Company introduction" : lang==="cn" ? "公司简介" : "회사소개" }</li>
                            <li>{lang==="en" ? "Personal policy" : lang==="cn" ? "个人信息处理方针" : "개인정보처리방침" }</li>
                            <li>{lang==="en" ? "Terms and Conditions" : lang==="cn" ? "使用条款" : "이용약관" }</li>
                            <li>{lang==="en" ? "Instructions for use" : lang==="cn" ? "使用指南" : "이용안내" }</li>
                        </ul>
                    </div>
                </div>
                <div className="footer-bottom">
                    <div className="footer-info">
                        <p>
                        {lang==="en" ? "STEP UP is a video platform that creates a new dance experience. In this space for dance enthusiasts, unleash your passion and rhythm freely! Explore the world of dance and express yourself with STEP UP!" : 
                        lang==="cn" ? "STEP UP是一个为舞蹈爱好者创造全新舞蹈体验的视频平台。在这个专属于热爱舞蹈的人们的空间里，自由展现您的激情与节奏！用STEP UP探索舞蹈世界，尽情表达自己！"
                        : "STEP UP은 춤의 새로운 경험을 만들어내는 화상 플랫폼입니다. 춤을 사랑하는 사람들을 위한 이 공간에서, 당신의 열정과 리듬을 자유롭게 펼치세요!" }
                            
                        </p>
                    </div>
                    <div className="copyright">
                        <p>Copyright 2017. SSAFY. All rights reserved.</p>
                    </div>
                </div>
            </footer>
        </>
    )
}

export default Footer;