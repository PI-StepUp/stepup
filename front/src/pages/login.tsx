import React, {useState} from "react"
import Header from "../components/Header"
import Footer from "../components/Footer"
import LanguageButton from "components/LanguageButton"

import { useRecoilState } from "recoil";
import { axiosUser } from "apis/axios";
import { useRouter } from "next/router";

import { LanguageState, accessTokenState, refreshTokenState } from "states/states";

const Login = () => {
    const [id, setId] = useState('');
    const [password, setPassword] = useState('');
    const [lang, setLang] = useRecoilState(LanguageState);
    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);

    const router = useRouter();

    const login = async () => {
        try{
            const user = await axiosUser.post("/login", {
                id: id,
                password: password,
            })
            console.log(user);
            if(user.data.message === "로그인 완료"){
                setAccessToken(user.data.data.accessToken);
                setRefreshToken(user.data.data.refreshToken);
                router.push('/');
            }
        }catch(e){
            alert("가입되지 않은 고객입니다. 회원가입 후 이용해주세요.");
        }
    }
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
                                <input type="text" placeholder={lang==="en" ? "ID" : lang==="cn" ? "用户名" : "아이디" } name="id" onChange={(e) => setId(e.target.value)}/>
                                <input type="password" placeholder={lang==="en" ? "PASSWORD" : lang==="cn" ? "密码" : "비밀번호" } name="password" onChange={(e) => setPassword(e.target.value)}/>
                                <div className="flex-box">
                                    <label htmlFor=""><input type="checkbox" />{lang==="en" ? "Save ID" : lang==="cn" ? "保存用户名" : "아이디 저장" }</label>
                                    <ul>
                                        <li>{lang==="en" ? "Sign up for membership" : lang==="cn" ? "注册会员" : "회원가입하기" }</li>
                                        <div className="separate-vertical-line"></div>
                                        <li>{lang==="en" ? "Find a password" : lang==="cn" ? "找回密码" : "비밀번호찾기" }</li>
                                    </ul>
                                </div>
                            </form>
                            
                            <button onClick={login}>{lang==="en" ? "LOGIN" : lang==="cn" ? "注册" : "로그인" }</button>
                        </div>
                    </div>
                </div>
                <LanguageButton/>
            <Footer/>
        </>
    )
}

export default Login;