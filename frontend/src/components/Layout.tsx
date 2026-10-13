import { AppBar, Toolbar, Typography, Tabs, Tab } from '@mui/material';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import styles from './Layout.module.css';

const NAV_ITEMS = [
    { label: 'LabWork', path: '/' },
    { label: 'Discipline', path: '/disciplines' },
    { label: 'Location', path: '/locations' },
    { label: 'Person', path: '/people' },
    { label: 'Спецоперации', path: '/special-operations' },
];

function Layout() {
    const location = useLocation();
    const navigate = useNavigate();
    const currentTab =
        NAV_ITEMS.find((item) => item.path === location.pathname)?.path ??
        false;

    return (
        <>
            <AppBar position="sticky" elevation={0}>
                <Toolbar>
                    <Typography variant="h6" className={styles.brand}>
                        Information Systems Lab 1
                    </Typography>
                </Toolbar>
                <Tabs
                    value={currentTab}
                    onChange={(_, value: string) => navigate(value)}
                    className={styles.tabs}
                >
                    {NAV_ITEMS.map((item) => (
                        <Tab
                            key={item.path}
                            label={item.label}
                            value={item.path}
                        />
                    ))}
                </Tabs>
            </AppBar>
            <Outlet />
        </>
    );
}

export default Layout;
