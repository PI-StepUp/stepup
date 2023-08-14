import { useEffect, useState } from "react"
import ApexChart from "apexcharts"
import dynamic from "next/dynamic"
const Chart = dynamic(() => import('react-apexcharts'), {ssr:false})

import { axiosDance, axiosMusic } from "apis/axios"
import { useRecoilState } from "recoil"
import { roleState } from "states/states"

const Admin = () => {
    const [role, setRole] = useRecoilState(roleState);
    const [showPage, setShowPage] = useState<Boolean>(false);

    const openTimeOptions = {
        chart: {
          type: 'bar',
          height: 430
        },
        plotOptions: {
          bar: {
            horizontal: true,
            dataLabels: {
              position: 'top',
            },
          }
        },
        dataLabels: {
          enabled: true,
          offsetX: -6,
          style: {
            fontSize: '10px',
            colors: ['#fff']
          }
        },
        stroke: {
          show: true,
          width: 1,
          colors: ['#fff']
        },
        tooltip: {
          shared: true,
          intersect: false
        },
        xaxis: {
          categories: ["0시", "1시", "2시", "3시", "4시", "5시", "6시", "7시", "8시", "9시", "10시", "11시"],
        },
      }
    const openTimeSeries = [{
        name: "오전 랜플댄 개최 시간",
        data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    }, {
        name: "오후 랜플댄 개최 시간",
        data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    }];

    const musicOptions = {
        chart: {
            width: 380,
            type: 'polarArea'
        },
        labels: ["비어있음"],
        fill: {
            opacity: 1
        },
        stroke: {
            width: 1,
            colors: undefined
        },
        yaxis: {
            show: false
        },
        legend: {
            position: 'bottom'
        },
        plotOptions: {
            polarArea: {
              rings: {
                strokeWidth: 0
              },
              spokes: {
                strokeWidth: 0
              },
            }
        },
        theme: {
            monochrome: {
              enabled: true,
              shadeTo: 'light',
              shadeIntensity: 0.6
            }
        } 
    };

    const musicSeries = [0];
    
    useEffect(() => {
            axiosDance.get("",{
                params:{
                    progressType: "ALL",
                }
            }).then((data) => {
                let todayMonth = new Date().getMonth() + 1;
                let todayDay = new Date().getDate();
                let dataDate = data.data.data;
                for(let i=0; i<dataDate.length; i++){
                    if(Number(dataDate[i].startAt.split("T")[0].split("-")[1]) === todayMonth && Number(dataDate[i].startAt.split("T")[0].split("-")[2]) === todayDay){
                        if(Number(dataDate[i].startAt.split("T")[1].split(":")[0]) < 12){
                            openTimeSeries[0].data[dataDate[i].startAt.split("T")[1].split(":")[0]] = openTimeSeries[0].data[dataDate[i].startAt.split("T")[1].split(":")[0]] + 1;
                        }else if(Number(dataDate[i].startAt.split("T")[1].split(":")[0]) >= 12){
                            openTimeSeries[1].data[dataDate[i].startAt.split("T")[1].split(":")[0]] = openTimeSeries[1].data[dataDate[i].startAt.split("T")[1].split(":")[0]] + 1;
                        }
                    }
                }
            })
    
            axiosMusic.get("/apply",{
                params:{
                    keyword: "",
                }
            }).then((data) => {
                let dataMusic = data.data.data;
                let dataMap = new Map();
                for(let i=0; i<dataMusic.length; i++){
                    if(dataMap.get(dataMusic[i].artist) === undefined){
                        dataMap.set(dataMusic[i].artist, 1);
                    }else{
                        dataMap.set(dataMusic[i].artist, dataMap.get(dataMusic[i].artist) + 1);
                    }
                }
                console.log(dataMap);
                dataMap.forEach((value, key, mapObject) => {
                    musicOptions.labels.push(key);
                    musicSeries.push(value);
                })
            })
    }, []);

    useEffect(() => {
        if (role === "ROLE_ADMIN") {
            setShowPage(true);
        } else {
            setShowPage(false);
        }
    }, [role]);

    
    return (
        <>
        {
            showPage ?
            <div className="admin-wrap">
                <div className="admin-header">
                    <div className="admin-header-center-block">
                        <span>STEPUP</span>
                        <h1>관리자님, 환영합니다.</h1>
                    </div>
                </div>
                <nav>
                    <ul>
                        <li>랜플댄 사용 통계</li>
                    </ul>
                </nav>
                <div className="chart-bg">
                    <div className="chart-main">
                        <section>
                            <h3>랜플댄 개최시간별 통계</h3>
                            <Chart
                                options={openTimeOptions}
                                series={openTimeSeries}
                                type="bar"
                                height={430}
                            />
                        </section>
                        <section>
                            <h3>사용자 선호 아티스트 통계</h3>
                            <Chart
                                options={musicOptions}
                                series={musicSeries}
                                type="polarArea"
                                width={380}
                            />
                        </section>
                    </div>
                </div>
            </div>
            :
            <div>접근권한이 없습니다.</div>
        }
        </>
    )
}

export default Admin;