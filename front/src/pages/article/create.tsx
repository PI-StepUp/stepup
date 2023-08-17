import { useRef } from "react";
import { S3 } from 'aws-sdk';
import fs from 'fs';
import AWS from '../../../aws-config';

import Header from "components/Header";
import MainBanner from "components/MainBanner";
import SubNav from "components/subNav";
import Footer from "components/Footer";

import { axiosBoard, axiosUser } from "apis/axios";
import { useRouter } from "next/router";

import { accessTokenState, refreshTokenState, idState, nicknameState } from "states/states";
import { useRecoilState } from "recoil";

const ArticleCreate = () => {
    const title = useRef<any>();
    const content = useRef<any>();
    const file = useRef<any>();
    const router = useRouter();
    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);
    const [nickname, setNickname] = useRecoilState(nicknameState);
    const s3 = new S3();

    const filePath = `/article/${file.current.value}`;
    const bucketName = 'stepup-pi';
    const fileName = `${file.current.value}`;

    async function uploadFileToS3(bucketName: any, fileName: any, filePath: any) {
        const fileContent = fs.readFileSync(filePath);

        const params = {
            Bucket: bucketName,
            Key: fileName,
            Body: fileContent
        };

        try {
            const data = await s3.upload(params).promise();
            console.log('File uploaded successfully:', data.Location);
        } catch (err) {
            alert('파일 업로드 실패, 관리자에게 문의하세요.');
            console.error('Error uploading file:', err);
        }
    }

    const createArticle = async (e: any) => {
        e.preventDefault();
        try{
            await axiosBoard.post('/talk', {
                title: title.current.value,
                content: content.current.value,
                fileURL: file.current.value,
            }, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                }
            }).then((data) => {
                if(data.data.message === "자유게시판 등록 완료"){
                    alert("글 등록이 완료되었습니다.");
                    router.push('/article/list');
                }
            }).catch((error: any) => {
                if(error.response.data.message === "만료된 토큰"){
                    axiosBoard.post('/talk', {
                        title: title.current.value,
                        content: content.current.value,
                        fileURL: file.current.value,
                    },{
                        headers:{
                            refreshToken: refreshToken
                        }
                    }).then((data) => {
                        if(data.data.message === "토큰 재발급 완료"){
                            setAccessToken(data.data.data.accessToken);
                            setRefreshToken(data.data.data.refreshToken);
                        }
                    }).then(() => {
                        axiosBoard.post('/talk', {
                            title: title.current.value,
                            content: content.current.value,
                            fileURL: file.current.value,
                        },{
                            headers:{
                                Authorization: `Bearer ${accessToken}`,
                            }
                        }).then((data) => {
                            if(data.data.message === "자유게시판 등록 완료"){
                                alert("글 등록이 완료되었습니다.");
                                router.push('/article/list');
                            }
                        })
                    })
                }
            }).catch((data) => {
                if(data.response.status === 401){
                    alert("장시간 이용하지 않아 자동 로그아웃 되었습니다.");
                    setNickname("");
                    router.push("/login");
                    return;
                }

                if(data.response.status === 500){
                    alert("시스템 에러, 관리자에게 문의하세요.");
                    return;
                }
            })  
        }catch(e){
            alert("글 등록 실패, 관리자에게 문의하세요.");
        }
    }
    return (
        <>
            <Header />
            <MainBanner />
            <SubNav linkNo="2" />
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
                            <tbody>
                                <tr>
                                    <td>제목</td>
                                    <td><input type="text" placeholder="제목을 입력해주세요." className="input-title" ref={title} /></td>
                                </tr>
                                <tr>
                                    <td>내용</td>
                                    <td><textarea className="input-content" placeholder="내용을 입력해주세요." ref={content}></textarea></td>
                                </tr>
                                <tr>
                                    <td>첨부파일</td>
                                    <td><input type="file" accept="image/*" id="file-upload" ref={file} /></td>
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
                            </tbody>
                        </table>
                    </form>
                </div>
            </div>
            <Footer />
        </>
    )
}

export default ArticleCreate;