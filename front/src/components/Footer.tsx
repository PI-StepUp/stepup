import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

const Footer = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    return (
        <>
            <footer>
                <div className="footer-top">
                    <div className="footer-logo">
                        <h5>Footer logo</h5>
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
                        {lang==="en" ? "Safe for all users, regardless of device type High-quality video conferencing and video call capabilities It's a service" : 
                        lang==="cn" ? "与机器种类无关，对所有使用者来说都是安全的 优质的视频会议和视频通话功能 提供的服务" 
                        : "기기 종류와 관계없이 모든 사용자에게 안전하고 품질이 우수한 화상 회의와 영상 통화 기능을 제공하는 서비스입니다" }
                            
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