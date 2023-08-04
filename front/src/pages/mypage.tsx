import React, { useState, useRef, useEffect } from "react";
import Link from "next/link"

import Header from "../components/Header"
import Footer from "../components/Footer"
import Image from "next/image"
import img_profile from "/public/images/dummy-profile.png"
import img_rpdance from "/public/images/dummy-rpdance.png"
import img_notice from "/public/images/icon-notice.png"
import img_offline from "/public/images/icon-offline.png"
import img_requestsong from "/public/images/icon-requestsong.png"
import img_board from "/public/images/icon-board.png"
import img_settings from "/public/images/icon-settings.svg"
import img_vector from "/public/images/icon-vector.png"

import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";
import { accessTokenState, refreshTokenState, idState } from "states/states";

import { axiosUser, axiosDance, axiosBoard } from "apis/axios";

const MyPage = () => {
  const [lang, setLang] = useRecoilState(LanguageState);
  const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
  const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
  const [id, setId] = useRecoilState(idState);

  const [loginUser, setLoginUser] = useState<any>();
  const [reserved, setReserved] = useState<any[]>();

  // console.log("Language ")
  // console.log(lang);
  // console.log("Token ")
  // console.log(accessToken);

  // 작성한 게시글 목록으로 스크롤 이동
  const list = useRef<HTMLDivElement>(null);
  const scrollEvent = () => {
    list.current?.scrollIntoView({ behavior: "smooth", block: "start" });
  };

  useEffect(() => {
    try {
      axiosUser.post('/auth', {
        id: id,
      }, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
          refreshToken: refreshToken,
        }
      }).then((data) => {
        if (data.data.message === "토큰 재발급 완료") {
          setAccessToken(data.data.data.accessToken);
          setRefreshToken(data.data.data.refreshToken);
        }
      })
    } catch (e) {
      alert('시스템 에러, 관리자에게 문의하세요.');
    }

    // 로그인 유저 정보 조회
    axiosUser.get("", {
      headers: {
        Authorization: `Bearer ${accessToken}`
      }
    }).then((data) => {
      console.log("로그인 유저 정보 조회", data);
      if (data.data.message === "회원정보 조회 완료") {
        setLoginUser(data.data.data);
        console.log("로그인 유저 정보", loginUser);
      }
    })

    // 로그인 유저의 랜플댄 예약 목록 조회
    axiosDance.get("/my/reserve", {
      headers: {
        Authorization: `Bearer ${accessToken}`
      }
    }).then((data) => {
      console.log("로그인 유저 랜플댄 예약 목록 조회", data);
      if(data.data.message === "내가 예약한 랜덤 플레이 댄스 목록 조회 완료"){
        setReserved(data.data.data);
        console.log("내 예약 목록", reserved);
      }
    })
    
    // 로그인 유저가 작성한 정모 게시글 조회
    axiosBoard.get(`/meeting/my?id=${id}`, {
    }).then((data) => {
      console.log("로그인 유저가 작성한 정모 게시글 조회", data);
      

    })
  }, [])

  return (
    <>
      <Header />
      <div className="background-color">
        <div>
          <ul className="mypage-profile">
            <li>
              <div className="info">
                <div className="cnt">3</div>
                <div className="cnt-title">{lang === "en" ? "RESERVATION" : lang === "cn" ? "预订" : "예약된 랜플댄"}</div>
                <div className="btn-info">
                  <Link href="/randomplay/list">{lang === "en" ? "RPDance LIST" : lang === "cn" ? "舞蹈清单" : "랜플댄 목록 보기"}</Link>
                </div>
              </div>
            </li>
            <li>
              {/* 프로필 클릭 시 포인트 적립 내역 모달창 추가 필요 */}
              <div className="info history">
                <div className="img-profile">
                  <Image className="img" src={img_profile} alt="profile"></Image>
                </div>
                <div>
                  <progress value="985" max="2000"></progress>
                  <div className="progress-text">
                    <p>{lang === "en" ? "the next rank" : lang === "cn" ? "直到下一次排名" : "다음 랭킹까지"} 1,015</p>
                    <p>985/2,000</p>
                  </div>
                  <div className="info-user">
                    <p className="ranking">{lang === "en" ? "GOLD" : lang === "cn" ? "金子" : "골드"}</p>
                    <p className="name">김싸피</p>
                  </div>
                </div>
              </div>
            </li>
            <li>
              <div className="info">
                <div className="cnt">3</div>
                <div className="cnt-title">{lang === "en" ? "Number of POSTS" : lang === "cn" ? "撰写的帖子数量" : "작성한 글 수"}</div>
                <div className="btn-info">
                  <div onClick={scrollEvent}>{lang === "en" ? "CHECK POSTS" : lang === "cn" ? "检查帖子" : "게시글 확인하기"}</div>
                </div>
              </div>
            </li>
          </ul>
        </div>
        {/* end - profile information */}
        <div>
          <ul className="list">
            <div>{lang === "en" ? "My Reservation" : lang === "cn" ? "我的预订" : "내 예약"}</div>
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_rpdance} alt="reserved"></Image>
              </div>
              <div className="description">
                <div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
                <div className="title">여기서요? 4세대 남돌 곡 모음</div>
                <div className="learner">Learner</div>
              </div>
              <div className="btn-contents">
                <a href="">{lang === "en" ? "Host - Cancel" : lang === "cn" ? "取消活动" : "방 생성 취소"}</a>
                <a href="">{lang === "en" ? "Cancel" : lang === "cn" ? "取消参与" : "예약 취소"}</a>
              </div>
            </li>
            {/* end - one reservation*/}
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_rpdance} alt="reserved"></Image>
              </div>
              <div className="description">
                <div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
                <div className="title">여기서요? 4세대 남돌-여돌 곡 모음 제목이 길면 다음줄로. 최대 2줄</div>
                <div className="learner">Learner</div>
              </div>
              <div className="btn-contents">
                <a href="">{lang === "en" ? "Host - Cancel" : lang === "cn" ? "取消活动" : "방 생성 취소"}</a>
                <a href="">{lang === "en" ? "Cancel" : lang === "cn" ? "取消参与" : "예약 취소"}</a>
              </div>
            </li>
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_rpdance} alt="reserved"></Image>
              </div>
              <div className="description">
                <div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
                <div className="title">여기서요? 4세대 남돌-여돌 곡 모음 제목이 길면 다음줄로. 최대 2줄</div>
                <div className="learner">Learner</div>
              </div>
              <div className="btn-contents">
                <a href="">{lang === "en" ? "Host - Cancel" : lang === "cn" ? "取消活动" : "방 생성 취소"}</a>
                <a href="">{lang === "en" ? "Cancel" : lang === "cn" ? "取消参与" : "예약 취소"}</a>
              </div>
            </li>
          </ul>
        </div>
        {/* end - my reservation */}
        <div className="settings">
          <details>
            <summary>
              <Image src={img_settings} className="img-setting" alt="img_settings"></Image>
              <p className="settings-title">{lang === "en" ? "Edit My Info" : lang === "cn" ? "编辑会员信息" : "회원 정보 수정"}</p>
              <Image className="img-vector" src={img_vector} alt="img_arrow"></Image>
            </summary>
            <div className="enter-password">
              <p>{lang === "en" ? "Password" : lang === "cn" ? "密码" : "현재 비밀번호"}</p>
              <input className="pw" type="password" />
              <div className="btn-submit">
                <Link href="/mypageedit">{lang === "en" ? "Enter" : lang === "cn" ? "输入" : "입력"}</Link>
              </div>
            </div>
          </details>
        </div>
        {/* end - settings */}
        <div>
          <ul className="list">
            <div>참여 이력</div>
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_rpdance} alt="reserved"></Image>
              </div>
              <div className="description">
                <div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
                <div className="title-past">여기서요? 4세대 남돌 곡 모음</div>
                <div className="learner">Learner</div>
              </div>
              <div className="rank">
                <p>-</p>
              </div>
            </li>
            {/* end - one past random play dance*/}
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_rpdance} alt="reserved"></Image>
              </div>
              <div className="description">
                <div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
                <div className="title-past">여기서요? 4세대 남돌-여돌 곡 모음 제목이 길면 다음줄로. 최대 2줄</div>
                <div className="learner">Learner</div>
              </div>
              <div className="rank">
                <p>1위</p>
              </div>
            </li>
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_rpdance} alt="reserved"></Image>
              </div>
              <div className="description">
                <div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
                <div className="title-past">여기서요? 4세대 남돌-여돌 곡 모음 제목이 길면 다음줄로. 최대 2줄</div>
                <div className="learner">Learner</div>
              </div>
              <div className="rank">
                <p>3위</p>
              </div>
            </li>
            <a href="" className="btn-more">더보기</a>
          </ul>
        </div>
        {/* end - past random play dance */}
        <div ref={list}>
          <ul className="list">
            <div>작성한 글</div>
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_notice} alt="notice"></Image>
              </div>
              <div className="description">
                <div className="time">공지사항</div>
                <div className="title-past">작성한 공지사항 제목</div>
                <div className="learner">2023.07.15</div>
              </div>
              <div className="detail">
                <a href="">글 보기</a>
              </div>
            </li>
            {/* end - one board*/}
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_requestsong} alt="song"></Image>
              </div>
              <div className="description">
                <div className="time">플레이리스트</div>
                <div className="title-past">작성한 노래 신청글 제목 / 제목이 길면 다음줄로. 최대 2줄</div>
                <div className="learner">2023.07.15</div>
              </div>
              <div className="detail">
                <a href="">글 보기</a>
              </div>
            </li>
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_offline} alt="reserved"></Image>
              </div>
              <div className="description">
                <div className="time">오프라인 정모</div>
                <div className="title-past">작성한 오프라인 정모 게시글 제목 / 제목이 길면 다음줄로. 최대 2줄</div>
                <div className="learner">2023.07.15</div>
              </div>
              <div className="detail">
                <a href="">글 보기</a>
              </div>
            </li>
            <a href="" className="btn-more">더보기</a>
          </ul>
          {/* end - board */}
        </div>
        <div className="ad-enjoy">
          <div className="ad-title">
            <span>STEPUP의 </span>
            <span>더 많은 즐길거리 즐기기</span>
          </div>
          <div className="ad-btn">
            <a id="ad-whitebox" href="">랜플댄 예약하기</a>
            <a id="ad-bluebox" href="">개인연습실 이용하기</a>
          </div>
        </div>
      </div>
      {/* end - advertisement */}
      <Footer />
    </>
  )
}

export default MyPage;

function ModalInfo() {
  return {

  }
}