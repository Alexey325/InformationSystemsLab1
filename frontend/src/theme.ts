import { createTheme } from '@mui/material/styles';

const theme = createTheme({
    palette: {
        primary: {
            main: '#7A2E3A',
            light: '#9B4A56',
            dark: '#5C1F2A',
            contrastText: '#FFFFFF',
        },
        error: {
            main: '#B42318',
        },
        background: {
            default: '#FFFFFF',
            paper: '#FFFFFF',
        },
        text: {
            primary: '#1A1613',
            secondary: '#6B6358',
        },
    },
    shape: {
        borderRadius: 14,
    },
    typography: {
        fontFamily: 'system-ui, -apple-system, sans-serif',
        button: {
            textTransform: 'none',
            fontWeight: 600,
        },
    },
    components: {
        MuiPaper: {
            styleOverrides: {
                root: {
                    boxShadow:
                        '0 1px 2px rgba(0,0,0,0.04), 0 8px 24px rgba(0,0,0,0.08)',
                },
            },
        },
        MuiButton: {
            styleOverrides: {
                root: {
                    borderRadius: 10,
                },
                contained: {
                    boxShadow: '0 2px 6px rgba(122,46,58,0.35)',
                    '&:hover': {
                        boxShadow: '0 4px 12px rgba(122,46,58,0.45)',
                    },
                },
            },
        },
        MuiAppBar: {
            styleOverrides: {
                root: {
                    background: 'rgba(255,255,255,0.72)',
                    backdropFilter: 'blur(12px)',
                    color: '#1A1613',
                    boxShadow: '0 1px 0 rgba(0,0,0,0.06)',
                },
            },
        },
        MuiDialog: {
            styleOverrides: {
                paper: {
                    borderRadius: 18,
                    boxShadow: '0 20px 60px rgba(0,0,0,0.25)',
                },
            },
        },
        MuiTextField: {
            defaultProps: {
                size: 'small',
            },
        },
    },
});

export default theme;
