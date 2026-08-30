import styles from './Hero.module.css'

function Hero() {
    return (
        <section className={styles.hero}>
            <div className={styles.textColumn}>
                <p className={styles.eyebrow}>FRIZERIE. BĂRBIERIT. ATITUDINE</p>
                <h1 className={styles.h1}>Găsește-ți frizerul. Fără telefoane, fără așteptare.</h1>
                <p className={styles.subtext}>
                    Hairhub îți găsește frizeriile din zona ta, cu ore libere în timp real -
                    alegi serviciul, frizerul și momentul care ți se potrivește.
                </p>
                <button type={'button'} className={styles.ctaButton}>Găsește o frizerie</button>
            </div>
            <div className={styles.imagePlaceholder}></div>
        </section>
    )
}

export default Hero;