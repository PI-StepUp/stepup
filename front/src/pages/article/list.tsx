import {useState, useEffect, useRef} from "react";

import Header from "components/Header";
import MainBanner from "components/MainBanner";
import SubNav from "components/subNav";
import Footer from "components/Footer";
import LanguageButton from "components/LanguageButton";

import Link from "next/link";
import { useRouter } from "next/router";
import { useInView } from "react-intersection-observer";
import { useRecoilState } from "recoil";
import { LanguageState, boardIdState, nicknameState } from "states/states";
import { axiosBoard } from "apis/axios";

import Pagination from "react-js-pagination";

const ArticleList = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    const [articles, setArticles] = useState<any>();
    const [page, setPage] = useState<any>(1);
    const [nickname, setNickname] = useRecoilState(nicknameState);
    const [boardIdStat, setBoardIdStat] = useRecoilState(boardIdState);
    const searchValue = useRef<any>();
    const router = useRouter();
    const [articleTitle, inView] = useInView();
    const [writeBtnShow, setWriteBtnShow] = useState<Boolean>(false);

    const moveArticleDetail = (boardId : any) => {
        if(nickname === ""){
            alert("해당 서비스는 로그인 후 이용 가능합니다.");
            return;
        }

        setBoardIdStat(boardId);

        router.push({
            pathname: `/article/detail/${boardId}`,
        })
    }

    const handlePageChange = (page: any) => {
        setPage(page);
    }

    const searchArticles = async (e:any) => {
        e.preventDefault();
        axiosBoard.get("/talk", {
            params: {
                keyword: searchValue.current.value,
            }
        }).then((data) => {
            setArticles(data.data.data);
            setPage(1);
        })
    }

    useEffect(() => {
        if (nickname === "") {
            setWriteBtnShow(false);
        } else {
            setWriteBtnShow(true);
        }
    }, [nickname])
        
    useEffect(() => {
        axiosBoard.get("/talk", {
            params: {
                keyword: "",
            }
        }).then((data) => {
            setArticles(data.data.data);
        })
    }, [inView])

    return (
        <>
            <Header/>
            <MainBanner/>
            <SubNav linkNo="2"/>
            <div className="article-list-wrap">
                {
                    inView ?
                    <div className="article-list-title" style={{animationName: "left-animation"}} ref={articleTitle}>
                        <span>{lang==="en" ? "Article" : lang==="cn" ? "帖子" : "게시글" }</span>
                        <h3>
                            {lang==="en" ? "All the things KPOP needs" : lang==="cn" ? "KPOP所需的所有" : "KPOP에 필요한 모든" }<br/>
                            {lang==="en" ? "a place where we meet, step up" : lang==="cn" ? "相聚的地方，舞步舞动" : "만남이 모이는 곳, 스텝업" }<br/>
                            {lang==="en" ? "Article" : lang==="cn" ? "帖子" : "게시글" }
                        </h3>
                    </div>
                    :
                    <div className="article-list-title" ref={articleTitle}>
                        <span>{lang==="en" ? "Article" : lang==="cn" ? "帖子" : "게시글" }</span>
                        <h3>
                            {lang==="en" ? "All the things KPOP needs" : lang==="cn" ? "KPOP所需的所有" : "KPOP에 필요한 모든" }<br/>
                            {lang==="en" ? "a place where we meet, step up" : lang==="cn" ? "相聚的地方，舞步舞动" : "만남이 모이는 곳, 스텝업" }<br/>
                            {lang==="en" ? "Article" : lang==="cn" ? "帖子" : "게시글" }
                        </h3>
                    </div>
                }
                <div className="search-wrap">
                    <form>
                        <input type="text" placeholder={lang==="en" ? "Please enter a search term" : lang==="cn" ? "请输入搜索词" : "검색어를 입력해주세요" } ref={searchValue}/>
                        <input type="submit" value={lang==="en" ? "Search" : lang==="cn" ? "检索" : "검색" } onClick={searchArticles}/>
                    </form>
                </div>
                <table>
                    <colgroup>
                        <col width="10%"/>
                        <col width="10%"/>
                        <col width="65%"/>
                        <col width="15%"/>
                    </colgroup>
                    <thead>
                        <tr>
                            <th>NO</th>
                            <th>{lang==="en" ? "Writer" : lang==="cn" ? "作者" : "작성자" }</th>
                            <th>{lang==="en" ? "Title" : lang==="cn" ? "标题" : "제목" }</th>
                            <th>{lang==="en" ? "Number of Comments" : lang==="cn" ? "评论数量" : "댓글수" }</th>
                        </tr>
                    </thead>
                    <tbody>
                        {articles?.map((article: any , index: any) => {
                            if(index+1 <= page*10 && index+1 > page*10-10){
                                return(
                                    <tr onClick={() => moveArticleDetail(article.boardId)} key={index}>
                                        <td>{articles.length - index}</td>
                                        <td>{article.writerName}</td>
                                        <td>{article.title}</td>
                                        <td>{article.commentCnt}</td>
                                    </tr>
                                )
                            }
                        })}
                    </tbody>
                    
                </table>
                <div className="button-wrap">
                    {
                        writeBtnShow ?
                        <button><Link href="/article/create">{lang==="en" ? "CREATE" : lang==="cn" ? "撰写文章" : "글 작성하기" }</Link></button>
                        :
                        <></>
                    }
                </div>
                
                <div className="pagination">
                    <ul>
                        <Pagination
                            activePage={page}
                            itemsCountPerPage={10}
                            totalItemsCount={articles?.length}
                            pageRangeDisplayed={9}
                            prevPageText={'<'}
                            nextPageText={'>'}
                            onChange={handlePageChange}
                        />
                    </ul>
                </div>
            </div>
            <LanguageButton/>
            <Footer/>
        </>
    )
}

export default ArticleList;