import { createContext, useState, useEffect } from "react";
export const ThemeContext = createContext();
export const useTheme = () => {
    const [isDarkMode, setDarkMode] = useState(() => {
        const savedTheme = localStorage.getItem('theme');
        return savedTheme ? JSON.parse(savedTheme) : false;
    });
    const toggleTheme = () => {
        setDarkMode((prev) => {
            const newTheme = !prev;
            localStorage.setItem('theme', JSON.stringify(newTheme));
            return newTheme;
        });
    };

    useEffect(() => {
        localStorage.setItem('theme', JSON.stringify(isDarkMode));
    }, [isDarkMode]);

    return [isDarkMode, toggleTheme];
};
