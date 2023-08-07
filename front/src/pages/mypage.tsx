import React, { useState, useRef, useEffect } from "react";
import Link from "next/link"

import Header from "../components/Header"
import Banner from "components/MypageBanner";
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
import { useRouter } from "next/router";
import { LanguageState } from "states/states";
import { accessTokenState, refreshTokenState, idState } from "states/states";

import { axiosUser, axiosDance, axiosBoard, axiosRank } from "apis/axios";

const MyPage = () => {
  const [lang, setLang] = useRecoilState(LanguageState);
  const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
  const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
  const [id, setId] = useRecoilState(idState);

  const [loginUser, setLoginUser] = useState<any>();
  const [loginUserNickname, setLoginUserNickname] = useState<string>();
  const [rankName, setRankName] = useState<any>();
  const [profileImg, setProfileImg] = useState<string>("");
  const [point, setPoint] = useState<number>();
  const [pointLeft, setPointLeft] = useState<number>();
  const [pointHistory, setPointHistory] = useState<any[]>();
  const [reserved, setReserved] = useState<any[]>();
  const [reservedThumbnail, setReservedThumbnail] = useState<string[]>();
  const [randomDanceId, setRandomDanceId] = useState<any[]>();
  const [myRandomDance, setMyRandomDance] = useState<any[]>();
  const [myRandomDanceHistory, setMyRandomDanceHistory] = useState<any[]>();
  const [visibleItems, setVisibleItems] = useState(3);
  const [selectedRandomDance, setSelectedRandomDance] = useState<any>();
  const [meetingBoard, setMeetingBoard] = useState<any[]>();
  const [talkBoard, setTalkBoard] = useState<any[]>();

  const router = useRouter();
  const pwValue = useRef<any>();

  // console.log("Language ")
  // console.log(lang);
  // console.log("Token ")
  // console.log(accessToken);

  // 작성한 게시글 목록으로 스크롤 이동
  const list = useRef<HTMLDivElement>(null);
  const scrollEvent = () => {
    list.current?.scrollIntoView({ behavior: "smooth", block: "start" });
  };

  // 예약된 랜플댄 개수
  let reservedRandomDanceCnt: number = 0;

  // rank 관련 변수
  let rankBtnColor: string = "#A77044";
  let rankGoalPoint: number = 0;

  // 작성한 게시글 개수
  let boardCnt: number = 0;

  let cancelRandomDance: (arg0: any) => void;
  let deleteMyRandomDance: (arg0: any) => void;
  let checkPw;

  useEffect(() => {
    // 접근 권한(로그인 여부) 확인
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
        setLoginUserNickname(data.data.data.nickname);
        setRankName(data.data.data.rankName);
        switch (rankName) {
          case "브론즈":
            rankBtnColor = "#A77044";
            rankGoalPoint = 100;
            break;
          case "실버":
            rankBtnColor = "#A7A7AD";
            rankGoalPoint = 300;
            break;
          case "골드":
            rankBtnColor = "#FFB028";
            rankGoalPoint = 1000;
            break;
          case "플래티넘":
            rankBtnColor = "#86f989";
            rankGoalPoint = 9999;
            break;
        }

        setProfileImg(data.data.data.profileImg);
        if (profileImg === null) {
          setProfileImg("profileImg_default.png");
        }
        setPoint(data.data.data.point);
        setPointLeft(rankGoalPoint - data.data.data.point);
        console.log("로그인 유저 정보", loginUser);
      }
    })

    // 로그인 유저의 포인트 적립 내역 조회
    axiosRank.get("/my/history", {
      headers: {
        Authorization: `Bearer ${accessToken}`
      }
    }).then((data) => {
      console.log("로그인 유저의 포인트 적립 내역 조회", data);
      if (data.data.message === "포인트 적립 내역 조회 완료") {
        setPointHistory(data.data.data);
        console.log("포인트 적립 내역", pointHistory);
      }
    })

    // 로그인 유저가 작성한 정모 게시글 조회
    axiosBoard.get(`/meeting/my?id=${id}`, {
    }).then((data) => {
      console.log("로그인 유저가 작성한 정모 게시글 조회", data);
      if (data.data.message === "내가 작성한 정모 목록 조회") {
        setMeetingBoard(data.data.data);
        console.log("정모 작성 목록", meetingBoard);
      }
    })

    // 로그인 유저가 작성한 자유게시판 게시글 조회
    axiosBoard.get(`/talk/my?id=${id}`, {
    }).then((data) => {
      console.log("로그인 유저가 작성한 자유게시판 게시글 조회", data);
      if (data.data.message === "마이페이지 자유게시판 목록 조회 완료") {
        setTalkBoard(data.data.data);
        console.log("자유게시판 작성 목록", talkBoard);
      }
    })

    // 로그인 유저의 랜플댄 예약 목록 조회
    axiosDance.get("/my/reserve", {
      headers: {
        Authorization: `Bearer ${accessToken}`
      }
    }).then((data) => {
      console.log("로그인 유저 랜플댄 예약 목록 조회", data);
      if (data.data.message === "내가 예약한 랜덤 플레이 댄스 목록 조회 완료") {
        setReserved(data.data.data);
        reservedRandomDanceCnt = reserved?.length!;
        // setReservedThumbnail(data.data.data.thumbnail);
        console.log("내 예약 목록", reserved);
      }
    })

    // 로그인 유저가 개최한 랜플댄 목록 조회
    axiosDance.get("/my/open", {
      headers: {
        Authorization: `Bearer ${accessToken}`
      }
    }).then((data) => {
      console.log("로그인 유저가 개최한 랜플댄 목록", data);
      if (data.data.message === "내가 개최한 랜덤 플레이 댄스 목록 조회 완료") {
        setMyRandomDance(data.data.data);
        console.log("내 예약 목록", myRandomDance);
      }
    })

    // 로그인 유저가 개최한 랜플댄 수정
    // const editMyRandomDance = async () => {
    //   await axiosDance.put("/my", {
    //     headers: {
    //       Authorization: `Bearer ${accessToken}`
    //     },
    //     randomDanceId: myRandomDanceIdValue.current.value,
    //     title: myRandomDanceTitleValue.current.value,
    //     content: myRandomDanceContentValue.current.value,
    //     startAt: myRandomDanceStartAtValue.current.value,
    //     endAt: myRandomDanceEndAtValue.current.value,
    //     danceType: myRandomDanceTypeValue.current.value,
    //     maxUser: myRandomDanceMaxUserValue.current.value,
    //     thumbnail: myRandomDanceThumbnailValue.current.value,
    //     playlist: myRandomDancePlayListValue.current.value,
    //   }).then((data) => {
    //     if (data.data.message === "랜덤 플레이 댄스 수정 완료") {
    //       alert("랜덤 플레이 댄스 정보를 수정했습니다.");
    //     } else {
    //       alert("수정을 완료하지 못했습니다. 다시 한 번 시도해주세요.");
    //     }
    //   })
    // }

    // 랜플댄 아이디로 정보 조회 (API 존재 X)

    // 로그인 유저가 참여한 랜플댄 목록 조회
    axiosDance.get("/my/attend/", {
      headers: {
        Authorization: `Bearer ${accessToken}`
      },
    }).then((data) => {
      if (data.data.message === "내가 참여한 랜덤 플레이 댄스 목록 조회 완료") {
        setMyRandomDanceHistory(data.data.data);
        console.log("로그인 유저가 참여한 랜플댄 목록 조회", myRandomDanceHistory);
      }
    })

    // 작성한 게시글 개수 합산
    boardCnt = meetingBoard?.length + talkBoard?.length;



  }, [])

  useEffect(() => {

    // 로그인 유저의 랜플댄 예약 취소
    cancelRandomDance = async (selectedId: any) => {
      await axiosDance.delete(`/my/reserve/${selectedId}`, {
        headers: {
          Authorization: `Bearer ${accessToken}`
        }
      }).then((data) => {
        if (data.data.message === "랜덤 플레이 댄스 예약 취소 완료") {
          alert("예약을 취소하셨습니다.")
        } else {
          alert("예약을 취소하지 못했습니다. 다시 한 번 시도해주세요.")
        }
      })
    }

    // 로그인 유저가 개최한 랜플댄 삭제
    deleteMyRandomDance = async () => {
      await axiosDance.delete(`/my/${selectedRandomDance}`, {
        headers: {
          Authorization: `Bearer ${accessToken}`
        },
      }).then((data) => {
        if (data.data.message === "랜덤 플레이 댄스 삭제 완료") {
          alert("개최한 랜덤 플레이 댄스를 삭제했습니다.");
        } else {
          alert("삭제를 하지 못했습니다. 다시 한 번 시도해주세요.");
        }
      })
    }

    // 회원 정보 수정 - 비밀번호 일치 확인
    checkPw = async () => {
      await axiosUser.post("/checkpw", {
        headers: {
          Authorization: `Bearer ${accessToken}`
        },
        id: id,
        password: pwValue.current.value,
      }).then((data) => {
        if (data.data.message === "비밀번호 일치") {
          console.log("비밀번호 일치");
          router.push('/mypageedit');
        } else {
          console.log("비밀번호 불일치");
          alert("비밀번호가 일치하지 않습니다. 다시 한 번 입력해주세요.");
        }
      })
    }


  })

  return (
    <>
      <Header />
      <Banner />
      <div className="background-color">
        <div>
          <ul className="mypage-profile">
            <li>
              <div className="info">
                <div className="cnt">{reservedRandomDanceCnt}</div>
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
                  <Image className="img" src={profileImg} alt="profile"></Image>
                </div>
                <div>
                  <progress value={point} max={rankGoalPoint}></progress>
                  <div className="progress-text">
                    <p>{lang === "en" ? "the next rank" : lang === "cn" ? "直到下一次排名" : "다음 랭킹까지"} {pointLeft}</p>
                    <p>{point}/{rankGoalPoint}</p>
                  </div>
                  <div className="info-user">
                    <p className="ranking" style={{ backgroundColor: rankBtnColor }}>{rankName === "브론즈" ? "BRONZE" : rankName === "실버" ? "SILVER" : rankName === "골드" ? "GOLD" : "PLATINUM"}</p>
                    <p className="name">{loginUserNickname}</p>
                  </div>
                </div>
              </div>
            </li>
            <li>
              <div className="info">
                <div className="cnt">{boardCnt}</div>
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
            <div>
              {reserved?.map((reservation) => {
                const reservationId = reservation.randomDanceId;
                return (
                  <li className="contents">
                    <div className="img-box">
                      <Image className="img" src={reservation.thumbnail} alt="reserved"></Image>
                    </div>
                    <div className="description">
                      <div className="time">Korea {reservation.startAt} ~ {reservation.endAt} (KST)</div>
                      <div className="title">{reservation.title}</div>
                      <div className="learner">{reservation.danceType}</div>
                    </div>
                    <div className="btn-contents">
                      <a onClick={() => cancelRandomDance({ reservationId })}>{lang === "en" ? "Cancel" : lang === "cn" ? "取消参与" : "예약 취소"}</a>
                    </div>
                  </li>
                )
              })}
            </div>
          </ul>
        </div>
        {/* end - my reservation */}
        <div>
          <ul className="list">
            <div>{lang === "en" ? "My Organized" : lang === "cn" ? "我举办的活动" : "개최한 랜플댄"}</div>
            <div>
              {myRandomDance?.map((randomDance) => {
                const randomDanceId = randomDance.randomDanceId;
                return (
                  <li className="contents">
                    <div className="img-box">
                      <Image className="img" src={randomDance.thumbnail} alt="reserved"></Image>
                    </div>
                    <div className="description">
                      <div className="time">Korea {randomDance.startAt} ~ {randomDance.endAt} (KST)</div>
                      <div className="title">{randomDance.title}</div>
                      <div className="learner">{randomDance.danceType}</div>
                    </div>
                    <div className="btn-contents">
                      <a onClick={() => deleteMyRandomDance({ randomDanceId })}>{lang === "en" ? "Host - Cancel" : lang === "cn" ? "取消活动" : "방 생성 취소"}</a>
                    </div>
                  </li>
                )
              })}
            </div>
          </ul>
        </div>
        {/* end - my open */}
        <div className="settings">
          <details>
            <summary>
              <Image src={img_settings} className="img-setting" alt="img_settings"></Image>
              <p className="settings-title">{lang === "en" ? "Edit My Info" : lang === "cn" ? "编辑会员信息" : "회원 정보 수정"}</p>
              <Image className="img-vector" src={img_vector} alt="img_arrow"></Image>
            </summary>
            <div className="enter-password">
              <p>{lang === "en" ? "Password" : lang === "cn" ? "密码" : "현재 비밀번호"}</p>
              <input className="pw" type="password" ref={pwValue} />
              <div className="btn-submit" onClick={checkPw}>
                {lang === "en" ? "Enter" : lang === "cn" ? "输入" : "입력"}
              </div>
            </div>
          </details>
        </div>
        {/* end - settings */}
        <div>
          <ul className="list">
            <div>{lang === "en" ? "Participation History" : lang === "cn" ? "参与历史" : "참여 이력"}</div>
            <div>
              {myRandomDanceHistory?.slice(0, visibleItems)?.map((randomDance) => {
                const randomDanceId = randomDance.randomDanceId;
                return (
                  <li className="contents">
                    <div className="img-box">
                      <Image className="img" src={randomDance.thumbnail} alt="history"></Image>
                    </div>
                    <div className="description">
                      <div className="time">Korea {randomDance.startAt} ~ {randomDance.endAt} (KST)</div>
                      <div className="title">{randomDance.title}</div>
                      <div className="learner">{randomDance.danceType}</div>
                    </div>
                    <div className="rank">
                      <p>-</p>
                      {/* 2.2.6 API에 등수가 기록되지 않음 > 확인 필요 */}
                    </div>
                  </li>
                )
              })}
            </div>
          </ul>
          {myRandomDanceHistory && visibleItems < myRandomDanceHistory.length && (
            <button onClick={() => setVisibleItems(visibleItems + 3)}>
              {lang === "en" ? "View More" : lang === "cn" ? "查看更多" : "더보기"}
            </button>
          )}
        </div>
        {/* end - past random play dance */}
        <div ref={list}>
          <ul className="list">
            <div>{lang === "en" ? "Written Posts - Offline Meetings" : lang === "cn" ? "撰写的帖子 - 线下聚会" : "작성한 글 - 오프라인 정모"}</div>
            <div>
              {meetingBoard?.slice(0, visibleItems)?.map((board) => {
                const boardId = board.boardId;
                return (
                  <li className="contents">
                    <div className="img-box">
                      <Image className="img" src={img_offline} alt="history"></Image>
                    </div>
                    <div className="description">
                      <div className="time">{lang === "en" ? "Offline Meetings" : lang === "cn" ? "线下聚会" : "오프라인 정모"}</div>
                      <div className="title-past">{board.title}</div>
                      <div className="learner">comment {board.commentCnt}</div>
                    </div>
                    <div className="detail">
                      <a href="">{lang === "en" ? "View" : lang === "cn" ? "查看帖子" : "글 보기"}</a>
                      {/* 게시글 상세보기 url 확인 필요*/}
                    </div>
                  </li>
                )
              })}
            </div>
            {meetingBoard && visibleItems < meetingBoard.length && (
              <button onClick={() => setVisibleItems(visibleItems + 3)}>
                {lang === "en" ? "View More" : lang === "cn" ? "查看更多" : "더보기"}
              </button>
            )}
          </ul>
          {/* end - meeting board */}
          <ul className="list">
            <div>{lang === "en" ? "Written Posts - Free Board" : lang === "cn" ? "撰写的帖子 - 自由留言板" : "작성한 글 - 자유게시판"}</div>
            <div>
              {talkBoard?.slice(0, visibleItems)?.map((board) => {
                const boardId = board.boardId;
                return (
                  <li className="contents">
                    <div className="img-box">
                      <Image className="img" src={img_offline} alt="history"></Image>
                    </div>
                    <div className="description">
                      <div className="time">{lang === "en" ? "Offline Meetings" : lang === "cn" ? "线下聚会" : "오프라인 정모"}</div>
                      <div className="title-past">{board.title}</div>
                      <div className="learner">comment {board.commentCnt}</div>
                    </div>
                    <div className="detail">
                      <a href="">{lang === "en" ? "View" : lang === "cn" ? "查看帖子" : "글 보기"}</a>
                      {/* 게시글 상세보기 url 확인 필요*/}
                    </div>
                  </li>
                )
              })}
            </div>
            {talkBoard && visibleItems < talkBoard.length && (
              <button onClick={() => setVisibleItems(visibleItems + 3)}>
                {lang === "en" ? "View More" : lang === "cn" ? "查看更多" : "더보기"}
              </button>
            )}
          </ul>
          {/* end - board */}
        </div>
        <div className="ad-enjoy">
          <div className="ad-title">
            <span>{lang === "en" ? "STEP UP " : lang === "cn" ? "STEP UP " : "STEP UP의 "}</span>
            <span>{lang === "en" ? "Enjoy More Attractions" : lang === "cn" ? "更多乐趣体验" : "더 많은 즐길거리 즐기기"}</span>
          </div>
          <div className="ad-btn">
            <a id="ad-whitebox" href="">{lang === "en" ? "Reservation" : lang === "cn" ? "预约" : "랜플댄 예약하기"}</a>
            <a id="ad-bluebox" href="">{lang === "en" ? "Practice Room" : lang === "cn" ? "使用练习室" : "개인연습실 이용하기"}</a>
            {/* url 확인 필요 */}
          </div>
        </div>
      </div>
      {/* end - advertisement */}
      <Footer />
    </>
  )
}

export default MyPage;