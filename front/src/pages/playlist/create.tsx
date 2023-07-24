import Header from "components/Header";
import MainBanner from "components/MainBanner";
import SubNav from "components/subNav";
import Footer from "components/Footer";

const PlayListCreate = () => {
    return(
        <>
            <Header/>
            <MainBanner/>
            <SubNav/>
            <div className="create-wrap">
                <div className="create-title">
                    <span>게시글</span>
                    <div className="flex-wrap">
                        <h3>글 작성</h3>
                        <div className="horizontal-line"></div>
                    </div>
                </div>
                <div className="create-content">
                    <form action="">
                        <table>
                            <tr>
                                <td>노래 제목</td>
                                <td><input type="text" placeholder="노래 제목을 입력해주세요." className="input-title"/></td>
                            </tr>
                            <tr>
                                <td>가수</td>
                                <td><input type="text" placeholder="아티스트를 입력해주세요." className="input-title"/></td>
                            </tr>
                            <tr>
                                <td>신청 사유</td>
                                <td><textarea className="input-content" placeholder="내용을 입력해주세요."></textarea></td>
                            </tr>
                            <tr>
                                <td></td>
                                <td>
                                    <div className="create-button-wrap">
                                        <ul>
                                            <li><button>취소하기</button></li>
                                            <li><button>작성하기</button></li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
            <Footer/>
        </>
    )
}

export default PlayListCreate;