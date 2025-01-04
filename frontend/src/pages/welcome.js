import { Link } from 'react-router-dom';
import '../assets/styles/welcome.css';

function Welcome() {
    const getGreeting = () => {
        const hours = new Date().getHours();
        if (hours < 12) {
            return "Good Morning!";
        } else if (hours < 18) {
            return "Good Afternoon!";
        } else {
            return "Good Evening!";
        }
    };

    return (
        <div className="welcome-container">
            <nav className="navbar">
                <div className="navbar-left">
                    <Link to="/" className="company-name">ExpenseTracker</Link>
                </div>
                <div className="navbar-center">
                    <Link to="/" className="nav-link">Home</Link>
                    <Link to="/auth/login" className="nav-link">Login</Link>
                    <Link to="/auth/register" className="nav-link">Register</Link>
                    <Link to="/about" className="nav-link">About</Link>
                    <Link to="/contactus" className="nav-link">Contact Us</Link>
                </div>
            </nav>

            <div className="content-wrapper"><br/>
                <h1 className="welcome-title">Welcome to Expense Tracker</h1><br/>
                <h1 className="greeting-message fade-in">{getGreeting()}</h1>
                <p className="intro-text slide-in">
                    Welcome to the Expense Tracker, your trusted companion to easily manage and track your daily expenses. Take control of your finances with our intuitive tools and gain insights into your spending patterns.
                </p>
                <div className="button-container">
                    <Link to='/auth/login'>
                        <button className="btn-login">Log in</button>
                    </Link>
                    <Link to='/auth/register'>
                        <button className="btn-register">Create Account</button>
                    </Link>
                </div>
            </div>
            
            <footer className="footer">
                <p className="footer-text">&copy; 2024 Expense Tracker. All rights reserved.</p>
            </footer>
        </div>
    );
}

export default Welcome;
