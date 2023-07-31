import Header from "../components/Header"
import Footer from "../components/Footer"
import LanguageButton from "components/LanguageButton";

import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

const Login = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    return(
        <>
            <Header/>
                <div className="login-page-wrap">
                    <div className="login-block-center">
                        <div className="login-title">
                            <h3>{lang==="en" ? "LOGIN" : lang==="cn" ? "注册" : "로그인" }</h3>
                        </div>
                        <div className="login-content">
                            <form action="">
                                <input type="text" placeholder={lang==="en" ? "ID" : lang==="cn" ? "用户名" : "아이디" } name="id"/>
                                <input type="password" placeholder={lang==="en" ? "PASSWORD" : lang==="cn" ? "密码" : "비밀번호" } name="password"/>
                                <div className="flex-box">
                                    <label htmlFor=""><input type="checkbox" />{lang==="en" ? "Save ID" : lang==="cn" ? "保存用户名" : "아이디 저장" }</label>
                                    <ul>
                                        <li>{lang==="en" ? "Sign up for membership" : lang==="cn" ? "注册会员" : "회원가입하기" }</li>
                                        <div className="separate-vertical-line"></div>
                                        <li>{lang==="en" ? "Find a password" : lang==="cn" ? "找回密码" : "비밀번호찾기" }</li>
                                    </ul>
                                </div>
                            </form>
                            
                            <button>{lang==="en" ? "LOGIN" : lang==="cn" ? "注册" : "로그인" }</button>
                        </div>
                    </div>
                </div>
                <LanguageButton/>
            <Footer/>
        </>
    )
}

export default Login;