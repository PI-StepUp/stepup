import React, {useState, useRef} from "react"
import Header from "../components/Header"
import Footer from "../components/Footer"
import LanguageButton from "components/LanguageButton"

import { useRecoilState } from "recoil";
import { axiosUser } from "apis/axios";
import { useRouter } from "next/router";
import Image from "next/image";
import ModalCloseIcon from "/public/images/icon-modal-close.svg";

import { LanguageState, accessTokenState, refreshTokenState, idState, nicknameState, profileImgState, rankNameState } from "states/states";

const Login = () => {
    const [id, setId] = useState('');
    const [password, setPassword] = useState('');
    const [lang, setLang] = useRecoilState(LanguageState);
    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [idStat, setIdStat] = useRecoilState(idState);
    const [nickname, setNickname] = useRecoilState(nicknameState);
    const [profileImg, setProfileImg] = useRecoilState(profileImgState);
    const [rankName, setRankName] = useRecoilState(rankNameState);
    const emailValue = useRef<any>();
    const dateValue = useRef<any>();
    const modal = useRef<any>();
    const idValue = useRef<any>();
    const pwModal = useRef<any>();

    const router = useRouter();

    const findIdClick = () => {
        modal.current.style.display = "block";
    }

    const findPasswordClick = () => {
        pwModal.current.style.display = "block";
    }

    const findPw = async () => {
        await axiosUser.post('/findpw',{
            id: idValue.current.value,
            email: emailValue.current.value,
        }).then((data) => {
            if(data.data.message === "임시 비밀번호 전송 완료"){
                alert('임시 비밀번호를 메일로 발송해드렸습니다.');
            }
        })
    }

    const modalClose = () => {
        modal.current.style.display = "none";
    }

    const clickModalBack = () => {
        modal.current.style.display = "none";
    }

    const findId = async () => {
        await axiosUser.post('/findid',{
            email: emailValue.current.value,
            birth: dateValue.current.value,
        }).then((data) => {
            if(data.data.message === "아이디 전송 완료"){
                alert('아이디 정보를 보내드렸습니다. 이메일을 확인해주세요.');
            }
        })
    }

    const login = async () => {
        try{
            const user = await axiosUser.post("/login", {
                id: id,
                password: password,
            })
            if(user.data.message === "로그인 완료"){
                setAccessToken(user.data.data.tokens.accessToken);
                setRefreshToken(user.data.data.tokens.refreshToken);
                setIdStat(id);
                setNickname(user.data.data.userInfo.nickname);
                setProfileImg(user.data.data.userInfo.profileImg);
                setRankName(user.data.data.userInfo.rankName);
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
                                        <li onClick={findIdClick}>{lang==="en" ? "Find ID" : lang==="cn" ? "查找用户名" : "아이디찾기" }</li>
                                        <div className="separate-vertical-line"></div>
                                        <li onClick={findPasswordClick}>{lang==="en" ? "Find a password" : lang==="cn" ? "找回密码" : "비밀번호찾기" }</li>
                                    </ul>
                                </div>
                            </form>
                            
                            <button onClick={login}>{lang==="en" ? "LOGIN" : lang==="cn" ? "注册" : "로그인" }</button>
                        </div>
                    </div>
                </div>
                <LanguageButton/>
            <Footer/>
            <div className="modal-back" ref={modal} onClick={clickModalBack}>
                <div className="modal-main">
                    <div className="modal-title">
                        <h4>아이디 찾기</h4>
                        <Image src={ModalCloseIcon} alt="" onClick={modalClose}></Image>
                    </div>
                    <div className="modal-content">
                        <span>이메일로 찾기</span>
                        <input type="email" placeholder="이메일을 입력해주세요." ref={emailValue} />
                        <input type="date" placeholder="생년월일을 입력해주세요." ref={dateValue} />
                    </div>
                    <div className="modal-button-wrap">
                        <button onClick={findId}>아이디 찾기</button>
                    </div>
                </div>
            </div>
            <div className="modal-back" ref={pwModal} onClick={clickModalBack}>
                <div className="modal-main">
                    <div className="modal-title">
                        <h4>비밀번호찾기</h4>
                        <Image src={ModalCloseIcon} alt="" onClick={modalClose}></Image>
                    </div>
                    <div className="modal-content">
                        <span>아이디 & 이메일로 찾기</span>
                        <input type="text" placeholder="아이디를 입력해주세요." ref={idValue}/>
                        <input type="email" placeholder="이메일을 입력해주세요." ref={emailValue} />
                    </div>
                    <div className="modal-button-wrap">
                        <button onClick={findPw}>비밀번호 찾기</button>
                    </div>
                </div>
            </div>
        </>
    )
}

export default Login;