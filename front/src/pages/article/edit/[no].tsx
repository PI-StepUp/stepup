import {useRef, useEffect, useState} from "react";

import Header from "components/Header";
import MainBanner from "components/MainBanner";
import SubNav from "components/subNav";
import Footer from "components/Footer";

import { axiosBoard, axiosUser } from "apis/axios";
import { useRouter } from "next/router";

import { accessTokenState, refreshTokenState, idState } from "states/states";
import { useRecoilState } from "recoil";

const ArticleEdit = () => {
    const title = useRef<any>();
    const content = useRef<any>();
    const file = useRef<any>();
    const [article, setArticle] = useState<any>();
    const router = useRouter();
    const boardId = router.query.no;

    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);

    const createArticle = async (e:any) => {
        e.preventDefault();

        try{
            await axiosUser.post('/auth',{
                id: id,
            },{
                headers:{
                    Authorization: `Bearer ${accessToken}`,
                    refreshToken: refreshToken,
                }
            }).then((data) => {
                if(data.data.message === "토큰 재발급 완료"){
                    setAccessToken(data.data.data.accessToken);
                    setRefreshToken(data.data.data.refreshToken);
                }
            })
        }catch(e){
            alert('시스템 에러, 관리자에게 문의하세요.');
        }

        try{
            const create = await axiosBoard.put('/talk', {
                boardId: boardId,
                title: title.current.value,
                content: content.current.value,
                writerName: article.writerName,
                writerProfileImg: article.writerProfileImg,
                fileURL: file.current.value,
                boardType: article.boardType,
            },{
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                }
            })

            if(create.data.message === "자유게시판 수정 완료"){
                alert("글 수정이 완료되었습니다.");
                router.push('/article/list');
            }
        }catch(e){
            alert("글 등록 실패, 관리자에게 문의하세요.");
        }
    }

    useEffect(() => {
        try{
            axiosUser.post('/auth',{
                id: id,
            },{
                headers:{
                    Authorization: `Bearer ${accessToken}`,
                    refreshToken: refreshToken,
                }
            }).then((data) => {
                if(data.data.message === "토큰 재발급 완료"){
                    setAccessToken(data.data.data.accessToken);
                    setRefreshToken(data.data.data.refreshToken);
                }
            })
        }catch(e){
            alert('시스템 에러, 관리자에게 문의하세요.');
        }

        axiosBoard.get(`/talk/${boardId}`, {
            params:{
                boardId: Number(boardId),
            },
            headers:{
                Authorization: `Bearer ${accessToken}`,
            }
        }).then((data) => {
            console.log(data);
            if(data.data.message === "자유게시판 게시글 조회 완료"){
                title.current.value = data.data.data.title;
                content.current.value = data.data.data.content;
                file.current.value = data.data.data.fileURL;
                setArticle(data.data.data);
            }
        })
    }, [])
    return(
        <>
            <Header/>
            <MainBanner/>
            <SubNav linkNo="2"/>
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
                                <td><input type="text" placeholder="제목을 입력해주세요." className="input-title" ref={title}/></td>
                            </tr>
                            <tr>
                                <td>내용</td>
                                <td><textarea className="input-content" placeholder="내용을 입력해주세요." ref={content}></textarea></td>
                            </tr>
                            <tr>
                                <td>첨부파일</td>
                                <td><input type="file" accept="image/*" id="file-upload" ref={file}/></td>
                            </tr>
                            <tr>
                                <td></td>
                                <td>
                                    <div className="create-button-wrap">
                                        <ul>
                                            <li><button>취소하기</button></li>
                                            <li><button onClick={createArticle}>작성하기</button></li>
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

export default ArticleEdit;