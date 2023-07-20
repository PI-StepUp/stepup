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
import "../styles/noticecreate.scss"
import "../styles/randomplaylist.scss"
import "../styles/sidemenu.scss"
import "../styles/practiceroom.scss"

import type {AppProps} from 'next/app';
import Head from "next/head";

const StepUp = ({Component} : AppProps) => {
    return (
        <>
            <Head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            </Head>
            <Component/>
        </>
    )
}

export default StepUp;