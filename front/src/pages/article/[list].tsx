import Header from "components/Header";
import MainBanner from "components/MainBanner";
import SubNav from "components/subNav";
import Footer from "components/Footer";
import LanguageButton from "components/LanguageButton";

import Link from "next/link";

import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

const ArticleList = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    return (
        <>
            <Header/>
            <MainBanner/>
            <SubNav/>
            <div className="article-list-wrap">
                <div className="article-list-title">
                    <span>{lang==="en" ? "Article" : lang==="cn" ? "帖子" : "게시글" }</span>
                    <h3>
                        {lang==="en" ? "All the things KPOP needs" : lang==="cn" ? "KPOP所需的所有" : "KPOP에 필요한 모든" }<br/>
                        {lang==="en" ? "a place where we meet, step up" : lang==="cn" ? "相聚的地方，舞步舞动" : "만남이 모이는 곳, 스텝업" }<br/>
                        {lang==="en" ? "Article" : lang==="cn" ? "帖子" : "게시글" }
                    </h3>
                </div>
                <div className="search-wrap">
                    <form action="/">
                        <input type="text" placeholder={lang==="en" ? "Please enter a search term" : lang==="cn" ? "请输入搜索词" : "검색어를 입력해주세요" }/>
                        <input type="submit" value={lang==="en" ? "Search" : lang==="cn" ? "检索" : "검색" }/>
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
                            <th>{lang==="en" ? "Writer" : lang==="cn" ? "作者" : "작성자" }</th>
                            <th>{lang==="en" ? "Title" : lang==="cn" ? "标题" : "제목" }</th>
                            <th>{lang==="en" ? "Date of creation" : lang==="cn" ? "制定日期" : "작성일자" }</th>
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
                    <button><Link href="/article/create">{lang==="en" ? "CREATE" : lang==="cn" ? "撰写文章" : "글 작성하기" }</Link></button>
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
            <LanguageButton/>
            <Footer/>
        </>
    )
}

export default ArticleList;