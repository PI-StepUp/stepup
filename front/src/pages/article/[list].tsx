import Header from "components/Header";
import MainBanner from "components/MainBanner";
import SubNav from "components/subNav";
import Footer from "components/Footer";

import Link from "next/link";

const ArticleList = () => {
    return (
        <>
            <Header/>
            <MainBanner/>
            <SubNav/>
            <div className="article-list-wrap">
                <div className="article-list-title">
                    <span>게시글</span>
                    <h3>
                        KPOP에 필요한 모든<br/>
                        만남이 모이는 곳, 스텝업<br/>
                        게시글
                    </h3>
                </div>
                <div className="search-wrap">
                    <form action="/">
                        <input type="text" placeholder="검색어를 입력해주세요."/>
                        <input type="submit" value="검색"/>
                    </form>
                </div>
                <table>
                    <colgroup>
                        <col width="10%"/>
                        <col width="10%"/>
                        <col width="70%"/>
                        <col width="10%"/>
                    </colgroup>
                    <thead>
                        <tr>
                            <th>NO</th>
                            <th>작성자</th>
                            <th>제목</th>
                            <th>작성일자</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>10</td>
                            <td>김싸피</td>
                            <td>10번째게시글입니다.</td>
                            <td>2013.07.15</td>
                        </tr>
                        <tr>
                            <td>10</td>
                            <td>김싸피</td>
                            <td>10번째게시글입니다.</td>
                            <td>2013.07.15</td>
                        </tr>
                        <tr>
                            <td>10</td>
                            <td>김싸피</td>
                            <td>10번째게시글입니다.</td>
                            <td>2013.07.15</td>
                        </tr>
                        <tr>
                            <td>10</td>
                            <td>김싸피</td>
                            <td>10번째게시글입니다.</td>
                            <td>2013.07.15</td>
                        </tr>
                        <tr>
                            <td>10</td>
                            <td>김싸피</td>
                            <td>10번째게시글입니다.</td>
                            <td>2013.07.15</td>
                        </tr>
                        <tr>
                            <td>10</td>
                            <td>김싸피</td>
                            <td>10번째게시글입니다.</td>
                            <td>2013.07.15</td>
                        </tr>
                        <tr>
                            <td>10</td>
                            <td>김싸피</td>
                            <td>10번째게시글입니다.</td>
                            <td>2013.07.15</td>
                        </tr>
                        <tr>
                            <td>10</td>
                            <td>김싸피</td>
                            <td>10번째게시글입니다.</td>
                            <td>2013.07.15</td>
                        </tr>
                        <tr>
                            <td>10</td>
                            <td>김싸피</td>
                            <td>10번째게시글입니다.</td>
                            <td>2013.07.15</td>
                        </tr>
                        <tr>
                            <td>10</td>
                            <td>김싸피</td>
                            <td>10번째게시글입니다.</td>
                            <td>2013.07.15</td>
                        </tr>
                    </tbody>
                    
                </table>
                <div className="button-wrap">
                    <button><Link href="/article/create">글 작성하기</Link></button>
                </div>
                
                <div className="pagination">
                    <ul>
                        <li>1</li>
                        <li>2</li>
                        <li>3</li>
                        <li>4</li>
                        <li>5</li>
                        <li>6</li>
                        <li>7</li>
                        <li>8</li>
                    </ul>
                </div>
            </div>
            <Footer/>
        </>
    )
}

export default ArticleList;