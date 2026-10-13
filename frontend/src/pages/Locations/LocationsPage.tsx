import { useEffect, useState } from 'react';
import {
    Paper,
    Typography,
    Button,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    IconButton,
    Alert,
    CircularProgress,
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { deleteLocation, fetchLocations } from '../../api/locations';
import type { Location } from '../../types/location';
import ConfirmDialog from '../../components/ConfirmDialog';
import LocationDialog from './LocationDialog';
import styles from './LocationsPage.module.css';

function LocationsPage() {
    const [locations, setLocations] = useState<Location[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [refreshKey, setRefreshKey] = useState(0);

    const [dialogOpen, setDialogOpen] = useState(false);
    const [editingLocation, setEditingLocation] = useState<Location | null>(
        null,
    );
    const [deleteTarget, setDeleteTarget] = useState<Location | null>(null);

    useEffect(() => {
        setLoading(true);
        setError(null);
        fetchLocations()
            .then(setLocations)
            .catch((err: Error) => setError(err.message))
            .finally(() => setLoading(false));
    }, [refreshKey]);

    function handleAdd() {
        setEditingLocation(null);
        setDialogOpen(true);
    }

    function handleEdit(l: Location) {
        setEditingLocation(l);
        setDialogOpen(true);
    }

    function handleSaved() {
        setRefreshKey((k) => k + 1);
    }

    function handleDeleteClick(l: Location) {
        setDeleteTarget(l);
    }

    function handleConfirmDelete() {
        if (!deleteTarget) return;
        deleteLocation(deleteTarget.id)
            .then(() => setRefreshKey((k) => k + 1))
            .catch((err: Error) => setError(err.message))
            .finally(() => setDeleteTarget(null));
    }

    return (
        <div className={styles.page}>
            <Paper className={styles.headerCard}>
                <Typography variant="h5" className={styles.title}>
                    Location
                </Typography>
                <span className={styles.totalBadge}>
                    Всего: {locations.length}
                </span>
            </Paper>

            <Paper className={styles.actionsCard}>
                <Button variant="contained" onClick={handleAdd}>
                    + Добавить Location
                </Button>
            </Paper>

            {error && <Alert severity="error">{error}</Alert>}

            <Paper className={styles.tableCard}>
                <TableContainer className={styles.tableScroll}>
                    <Table size="small" className={styles.table}>
                        <TableHead>
                            <TableRow>
                                <TableCell className={styles.headCell}>
                                    ID
                                </TableCell>
                                <TableCell
                                    className={styles.headCell}
                                    align="right"
                                >
                                    x
                                </TableCell>
                                <TableCell
                                    className={styles.headCell}
                                    align="right"
                                >
                                    y
                                </TableCell>
                                <TableCell
                                    className={styles.headCell}
                                    align="right"
                                >
                                    z
                                </TableCell>
                                <TableCell className={styles.headCell}>
                                    Name
                                </TableCell>
                                <TableCell className={styles.headCell}>
                                    People
                                </TableCell>
                                <TableCell
                                    className={styles.headCell}
                                    align="right"
                                >
                                    Действия
                                </TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {locations.map((l) => (
                                <TableRow key={l.id} hover>
                                    <TableCell>{l.id}</TableCell>
                                    <TableCell align="right">{l.x}</TableCell>
                                    <TableCell align="right">{l.y}</TableCell>
                                    <TableCell align="right">{l.z}</TableCell>
                                    <TableCell>{l.name ?? '—'}</TableCell>
                                    <TableCell className={styles.peopleCell}>
                                        {l.people
                                            .map((p) => p.name)
                                            .join(', ') || '—'}
                                    </TableCell>
                                    <TableCell align="right">
                                        <IconButton
                                            size="small"
                                            onClick={() => handleEdit(l)}
                                            aria-label={`Редактировать Location #${l.id}`}
                                        >
                                            <EditIcon fontSize="small" />
                                        </IconButton>
                                        <IconButton
                                            size="small"
                                            onClick={() => handleDeleteClick(l)}
                                            aria-label={`Удалить Location #${l.id}`}
                                        >
                                            <DeleteIcon fontSize="small" />
                                        </IconButton>
                                    </TableCell>
                                </TableRow>
                            ))}
                            {!loading && locations.length === 0 && (
                                <TableRow>
                                    <TableCell
                                        colSpan={7}
                                        align="center"
                                        className={styles.emptyCell}
                                    >
                                        Ничего не найдено
                                    </TableCell>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>

                {loading && (
                    <div className={styles.loadingRow}>
                        <CircularProgress size={24} />
                    </div>
                )}
            </Paper>

            <LocationDialog
                open={dialogOpen}
                location={editingLocation}
                onClose={() => setDialogOpen(false)}
                onSaved={handleSaved}
            />

            <ConfirmDialog
                open={deleteTarget !== null}
                title="Удалить Location?"
                message={`Удалить Location #${deleteTarget?.id}? Это действие необратимо.`}
                onConfirm={handleConfirmDelete}
                onCancel={() => setDeleteTarget(null)}
            />
        </div>
    );
}

export default LocationsPage;
