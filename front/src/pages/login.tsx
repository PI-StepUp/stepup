import Header from "../components/Header"
import Footer from "../components/Footer"

const Login = () => {
    return(
        <>
            <Header/>
                <div className="login-page-wrap">
                    <div className="login-block-center">
                        <div className="login-title">
                            <h3>LOGIN</h3>
                        </div>
                        <div className="login-content">
                            <form action="">
                                <input type="text" placeholder="아이디" name="id"/>
                                <input type="password" placeholder="비밀번호" name="password"/>
                                <div className="flex-box">
                                    <label htmlFor=""><input type="checkbox" />아이디 저장</label>
                                    <ul>
                                        <li>회원가입하기</li>
                                        <div className="separate-vertical-line"></div>
                                        <li>비밀번호찾기</li>
                                    </ul>
                                </div>
                            </form>
                            
                            <button>로그인</button>
                        </div>
                    </div>
                </div>
            <Footer/>
        </>
    )
}

export default Login;