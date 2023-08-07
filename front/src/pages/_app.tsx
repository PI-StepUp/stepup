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
import "../styles/MypageBanner.scss"
import "../styles/mypageedit.scss"
import "../styles/responsively.scss";
import "../styles/detail.scss";

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
                </Head>
                <RecoilRoot>
                    <Component/>
                </RecoilRoot>
            </QueryClientProvider>
        </>
    )
}

export default StepUp;