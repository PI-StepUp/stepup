import "../styles/global.scss"
import "../styles/header.scss"
import "../styles/main.scss"
import "../styles/mainBanner.scss"
import "../styles/subnav.scss"
import "../styles/noticelist.scss"
import "../styles/articlelist.scss"
import "../styles/meetinglist.scss"
import "../styles/footer.scss"
import "../styles/login.scss"
import "../styles/signup.scss"
import "../styles/playlist.scss"
import "../styles/create.scss"
import "../styles/randomplaylist.scss"
import "../styles/sidemenu.scss"
import "../styles/practiceroom.scss"
import "../styles/language.scss"
import "../styles/mypage.scss"
import "../styles/mypageBanner.scss"
import "../styles/mypageedit.scss"
import "../styles/mypageeditBanner.scss"
import "../styles/modal.scss"
import "../styles/responsively.scss";
import "../styles/detail.scss";
import "../styles/admin.scss";

import type {AppProps} from 'next/app';
import Head from "next/head";
import {RecoilRoot} from "recoil";
import {QueryClient, QueryClientProvider} from "react-query";

const queryClient = new QueryClient();

const StepUp = ({Component} : AppProps) => {
    return (
        <>
            <QueryClientProvider client={queryClient}>
                <Head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png"/>
                    <link rel="icon" type="image/png" sizes="32x32" href="/favicon-32x32.png"/>
                    <link rel="icon" type="image/png" sizes="16x16" href="/favicon-16x16.png"/>
                    <link rel="manifest" href="/site.webmanifest"/>
                    <meta name="msapplication-TileColor" content="#da532c"/>
                    <meta name="theme-color" content="#ffffff"></meta>
                    <title>STEPUP - KPOP에 필요한 모든 만남이 있는 곳</title>
                </Head>
                <RecoilRoot>
                    <Component/>
                </RecoilRoot>
            </QueryClientProvider>
        </>
    )
}

export default StepUp;