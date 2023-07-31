import Header from "components/Header";
import Footer from "components/Footer";
import LanguageButton from "components/LanguageButton";

import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

const SignUp = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    return(
        <>
            <Header/>
            <div className="signup-wrap">
                <div className="signup-block-center">
                    <div className="signup-title">
                        <h3>{lang==="en" ? "JOIN MEMBERSHIP" : lang==="cn" ? "注册会员" : "회원가입" }</h3>
                    </div>
                    <div className="signup-content">
                        <form action="/">
                            <div className="flex-wrap">
                                <input type="text" placeholder={lang==="en" ? "ID" : lang==="cn" ? "用户名" : "아이디" } className="input-id"/>
                                <button className="button-id">{lang==="en" ? "Dupl check" : lang==="cn" ? "重复确认" : "중복확인" }</button>
                            </div>
                            <div className="flex-wrap">
                                <input type="text" placeholder={lang==="en" ? "Nickname" : lang==="cn" ? "用户名" : "닉네임" } className="input-nickname"/>
                                <button className="button-password">{lang==="en" ? "Dupl check" : lang==="cn" ? "重复确认" : "중복확인" }</button>
                            </div>
                            <div className="flex-wrap">
                                <input type="email" placeholder={lang==="en" ? "email" : lang==="cn" ? "电子邮件" : "이메일" } className="input-email"/>
                                <button className="button-email">{lang==="en" ? "Dupl check" : lang==="cn" ? "重复确认" : "중복확인" }</button>
                            </div>
                            <input type="password" placeholder={lang==="en" ? "Password" : lang==="cn" ? "密码" : "비밀번호" } className="input-password"/>
                            <input type="password" placeholder={lang==="en" ? "Password verification" : lang==="cn" ? "密码确认" : "비밀번호확인" } className="input-password-check"/>
                            <input type="text" placeholder={lang==="en" ? "Date of birth" : lang==="cn" ? "出生年月日" : "생년월일" } className="input-birthday"/>
                            <input type="text" placeholder={lang==="en" ? "Nation" : lang==="cn" ? "国家" : "국가" } className="input-region"/>
                            <textarea name="" id="" readOnly>
                                'STEP UP (이하 '회사'는) 고객님의 개인정보를 중요시하며, "정보통신망 이용촉진 및 정보보호"에 관한 법률을 준수하고 있습니다. 회사는 개인정보취급방침을 통하여 고객님께서 제공하시는 개인정보가 어떠한 용도와 방식으로 이용되고 있으며, 개인정보보호를 위해 어떠한 조치가 취해지고 있는지 알려드립니다. 회사는 개인정보취급방침을 개정하는 경우 웹사이트 공지사항(또는 개별공지)을 통하여 공지할 것입니다.
본 방침은 : 2023 년 07 월 19 일 부터 시행됩니다.
■ 개인정보의 수집 및 이용목적
회사는 수집한 개인정보를 다음의 목적을 위해 활용합니다.

- 서비스 제공에 관한 계약 이행 및 서비스 제공에 따른 요금정산 콘텐츠 제공 , 구매 및 요금 결제
- 고객 관리: 고객 서비스 이용에 따른 본인확인 , 개인 식별 , 불량회원의 부정 이용 방지와 비인가 사용 방지 , 가입 의사 확인 , 연령확인 , 불만처리 등 민원처리 , 고지사항 전달
- 마케팅 및 광고에 활용 : 신규 서비스(제품) 개발 및 특화, 이벤트 등 광고성 정보 전달, 접속 빈도 파악 또는 회원의 서비스 이용에 대한 통계


■ 수집하는 개인정보 항목
회사는 정보주체가 고객서비스(상담신청, 상담, 서비스 신청 등)를 이용하기 위하여 고객의 개인 정보를 제공할 때의 서비스 제공을 위한 필수 정보와, 보다 향상된 서비스 제공을 위한 선택정보를 온라인상 입력방법을 통하여 수집하고 있습니다. 수집하는 개인정보의 범위는 아래와 같습니다.

개인정보 수집항목
- 필수항목: 이름, 생년월일, 전화번호 , 이메일, 주소, 여권사본,
- 선택항목: 직업, 학력, 이력, 유학동기, 유학계획, 유학목적 등 입학 지원을 하기 위하여 해당 교육기관에서 요청하는 개인 정보.
- 자동수집 항목 : 접속로그( 접속시간, 접속 아이피,접속 웹브라우져종류,서비스 이용 기록 )

개인정보 수집방법
정보주체는 웹사이트 또는 서면 절차를 통하여 개인정보처리방침과 이용약관 각각의 내용을 확인하고 ‘동의’ 또는 ‘동의하지 않는다’ 문구를 선택 할 수 있습니다. 정보 주체가 ‘동의’ 문구를 선택한 경우에는 개인정보 수집에 동의한 것으로 봅니다.


■ 개인정보의 안전성 확보 조치
회사는 [개인정보보호법] 제29조에 따라 다음과 같이 안전성 확보에 필요한 기술적, 관리적 안전성 확보 조치를 취하고 있습니다.

관리적 안전성 확보 조치
- 개인정보의 안전한 처리를 위하여 ‘내부관리계획’을 수립하여 2012년 3월 1일자로 시행하고 있습니다.
- 회사 내부의 ‘Privacy Policy’를 수립하고 2012년 3월 1일자로 시행하여 개인정보보호를 강화하였습니다.
- 개인정보 처리 관련 안정성 확보를 위하여 정기적(년 1회)으로 자체적인 감사를 실시하고 있습니다.
- 개인정보를 처리하는 직원을 지정하고 담당자에 한정시켜 최소화 하여 개인정보를 관리하는 대책을 시행하고 있습니다.

기술적 안전성 확보 조치
- 회사는 해킹이나 컴퓨터 바이러스 등에 의한 개인정보 유출 및 훼손을 막기 위하여 보안프로그램을 설치하고 주기적인 갱신 및 점검을 하며 외부로부터 접근이 통제된 구역에 시스템을 설치하고 기술적/물리적으로 감시 및 차단하고 있습니다.
- 개인정보를 처리하는 데이터베이스시스템에 대한 접근권한의 부여,변경,말소를 통하여 개인정보에 대한 접근통제를 위하여 필요한 조치를 하고 있으며 침입차단시스템(방화벽)을 이용하여 외부로부터의 무단 접근을 통제하고 있습니다.
- 정보주체의 개인정보는 비밀번호는 암호화 되어 저장 및 관리되고 있어, 본인만이 알 수 있으며 중요한 데이터는 파일 및 전송 데이터를 암호화 하거나 파일 잠금 기능을 사용하는 등의 별도 보안기능을 사용하고 있습니다.

물리적 안전성 확보 조치
- 개인정보를 보관하고 있는 물리적 보관 장소를 별도로 두고 이에 대해 출입통제 절차를 수립, 운영하고 있습니다.
- 개인정보가 포함된 서류, 보조저장매체 등을 잠금 장치가 있는 안전한 장소에 보관하고 있습니다.


■ 동의를 거부할 권리 및 거부 시 불이익
위 개인정보 중 필수적 정보의 수집/이용에 관한 동의는 계약의 체결 및 이행을 위하여 필수적이므로, 위 사항에 동의하셔야만 서비스의 제공이 가능합니다. 위 개인정보 중 선택적 정보의 수집/이용에 관한 동의는 거부하실 수 있으며, 다만 동의하지 않으시는 경우 회원제 서비스의 이용이 제한됩니다.

■ 개인정보의 보유 및 이용기간
회사는 법령에 따른 개인정보 보유 이용기간 또는 정보주체로부터 개인정보를 수집 시에 동의 받은 개인정보 보유, 이용 기간 내에 한하여 개인정보를 보유 및 처리합니다.
제공된 개인 정보 폐기를 요청하거나 개인정보의 수집 및 이용에 대한 동의를 철회하는 경우, 수집 및 이용목적이 달성되거나 보유 및 이용기간이 종료한 경우 해당 개인정보를 지체 없이 파기합니다.

단, 다음의 정보에 대해서는 아래의 이유로 명시한 기간 동안 보존합니다.

가. 회사 내부 방침에 의한 정보보유 사유
- 내 용 : 상담 관련 정보
- 보존이유 : 고객에게 원활한 상담 서비스 제공
- 보존기간 : 폐기 요청 시 까지
                            </textarea>
                            <div className="agree-check-wrap">
                                <p>{lang==="en" ? "Do you agree to collect and use personal information?" : lang==="cn" ? "您同意收集和使用个人信息吗？" : "개인정보 수집 및 이용에 동의하십니까?" }</p>
                                <input type="checkbox" />
                            </div>
                            <textarea name="" id="" readOnly>
                                STEP UP은 개인정보보호법 등에 관한 법률 등 관계법령에 따라 광고성 정보를 전송하
기 위해 수신자의 사전 수신동의를 받고 있으며, 광고 성 정보 수신자의 수신동의여부를 정기적
으로 확인합니다. 다만 동의하지 않을 경우, 상품/서비스 소개 및 권유, 사은행사, 판촉행사 등 이
용목적에 따른 혜택의 제한이 있을 수 있습니다. 그 밖에 금융 거래와 관련된 불이익은 없습니다.
- 전송방법
고객님의 핸드폰 문자메시지(SMS), Email 등을 통해 전달될 수 있습니다.
- 전송내용
발송되는 마케팅 정보는 수신자에게 ㈜코나아이가(지역화폐 및 코나카드샵 등) 운영하는 서비스에
서 제공하는 혜택 정보, 각종 이벤트 정보, 상품 정보, 신규 서비스 안내 등 광고성 정보로 관련
법의 규정을 준수하여 발송됩니다. 단, 광고성 정보 이외 의무적으로 안내되어야 하는 정보성 내
용은 수신동의 여부와 무관하게 제공됩니다.
- 철회안내
고객님은 수신 동의 이후에라도 의사에 따라 동의를 철회할 수 있으며, 수신을 동의하지 않아도
회사가 제공하는 기본적인 서비스를 이용할 수 있으나, 당사의 마케팅 정보를 수신하지 못할 수
있습니다.
                            </textarea>
                            <div className="agree-check-wrap">
                                <p>{lang==="en" ? "Do you agree to receive an email?" : lang==="cn" ? "你是否同意接收电子邮件?" : "이메일 수신 여부에 동의하십니까?" }</p>
                                <input type="checkbox" />
                            </div>
                            <input type="submit" value={lang==="en" ? "JOIN MEMBERSHIP" : lang==="cn" ? "注册会员" : "회원가입" }/>
                        </form>
                    </div>
                </div>
            </div>
            <LanguageButton/>
            <Footer/>
        </>
    )
}

export default SignUp;