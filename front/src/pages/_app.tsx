import "../styles/global.scss"
import "../styles/header.scss"
import "../styles/main.scss"

import type {AppProps} from 'next/app';

const StepUp = ({Component} : AppProps) => {
    return (
        <>
            <Component/>
        </>
    )
}

export default StepUp;