

const Footer = () => {
    return (
        <>
            <footer>
                <div className="footer-top">
                    <div className="footer-logo">
                        <h5>Footer logo</h5>
                    </div>
                    <div className="footer-nav">
                        <ul>
                            <li>회사소개</li>
                            <li>개인정보처리방침</li>
                            <li>이용약관</li>
                            <li>이용안내</li>
                        </ul>
                    </div>
                </div>
                <div className="footer-bottom">
                    <div className="footer-info">
                        <p>
                            기기 종류와 관계없이 모든 사용자에게 안전하고
                            품질이 우수한 화상 회의와 영상 통화 기능을
                            제공하는 서비스입니다
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