import Header from "components/Header";
import MainBanner from "components/MainBanner";
import SubNav from "components/subNav";
import Footer from "components/Footer";

const MeetingCreate = () => {
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
                                <td>제목</td>
                                <td><input type="text" placeholder="제목을 입력해주세요." className="input-title"/></td>
                            </tr>
                            <tr>
                                <td>내용</td>
                                <td><textarea className="input-content" placeholder="내용을 입력해주세요."></textarea></td>
                            </tr>
                            <tr>
                                <td>모임 날짜</td>
                                <td><input type="date" placeholder="시간을 입력해주세요." className="input-date"/></td>
                            </tr>
                            <tr>
                                <td>모임 시간</td>
                                <td>
                                    <input type="time" placeholder="시간을 입력해주세요." className="input-time"/>- 
                                    <input type="time" placeholder="시간을 입력해주세요." className="input-time"/>
                                </td>
                            </tr>
                            <tr>
                                <td>지역</td>
                                <td><input type="text" placeholder="지역을 입력해주세요." className="input-region"/></td>
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

export default MeetingCreate;