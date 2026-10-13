import { ThemeProvider, CssBaseline } from '@mui/material';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import theme from './theme';
import Layout from './components/Layout';
import LabWorksPage from './pages/LabWorks/LabWorksPage';
import DisciplinesPage from './pages/Disciplines/DisciplinesPage';
import LocationsPage from './pages/Locations/LocationsPage';
import PeoplePage from './pages/People/PeoplePage';
import SpecialOperationsPage from './pages/SpecialOperationsPage';

function App() {
    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <BrowserRouter>
                <Routes>
                    <Route element={<Layout />}>
                        <Route path="/" element={<LabWorksPage />} />
                        <Route
                            path="/disciplines"
                            element={<DisciplinesPage />}
                        />
                        <Route path="/locations" element={<LocationsPage />} />
                        <Route path="/people" element={<PeoplePage />} />
                        <Route
                            path="/special-operations"
                            element={<SpecialOperationsPage />}
                        />
                    </Route>
                </Routes>
            </BrowserRouter>
        </ThemeProvider>
    );
}

export default App;
