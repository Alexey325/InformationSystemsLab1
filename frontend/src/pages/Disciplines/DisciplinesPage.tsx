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
import { deleteDiscipline, fetchDisciplines } from '../../api/disciplines';
import type { Discipline } from '../../types/discipline';
import ConfirmDialog from '../../components/ConfirmDialog';
import DisciplineDialog from './DisciplineDialog';
import styles from './DisciplinesPage.module.css';

function DisciplinesPage() {
    const [disciplines, setDisciplines] = useState<Discipline[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [refreshKey, setRefreshKey] = useState(0);

    const [dialogOpen, setDialogOpen] = useState(false);
    const [editingDiscipline, setEditingDiscipline] =
        useState<Discipline | null>(null);
    const [deleteTarget, setDeleteTarget] = useState<Discipline | null>(null);

    useEffect(() => {
        setLoading(true);
        setError(null);
        fetchDisciplines()
            .then(setDisciplines)
            .catch((err: Error) => setError(err.message))
            .finally(() => setLoading(false));
    }, [refreshKey]);

    function handleAdd() {
        setEditingDiscipline(null);
        setDialogOpen(true);
    }

    function handleEdit(d: Discipline) {
        setEditingDiscipline(d);
        setDialogOpen(true);
    }

    function handleSaved() {
        setRefreshKey((k) => k + 1);
    }

    function handleDeleteClick(d: Discipline) {
        setDeleteTarget(d);
    }

    function handleConfirmDelete() {
        if (!deleteTarget) return;
        deleteDiscipline(deleteTarget.id)
            .then(() => setRefreshKey((k) => k + 1))
            .catch((err: Error) => setError(err.message))
            .finally(() => setDeleteTarget(null));
    }

    return (
        <div className={styles.page}>
            <Paper className={styles.headerCard}>
                <Typography variant="h5" className={styles.title}>
                    Discipline
                </Typography>
                <span className={styles.totalBadge}>
                    Всего: {disciplines.length}
                </span>
            </Paper>

            <Paper className={styles.actionsCard}>
                <Button variant="contained" onClick={handleAdd}>
                    + Добавить Discipline
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
                                <TableCell className={styles.headCell}>
                                    Name
                                </TableCell>
                                <TableCell
                                    className={styles.headCell}
                                    align="right"
                                >
                                    Labs count
                                </TableCell>
                                <TableCell className={styles.headCell}>
                                    LabWorks
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
                            {disciplines.map((d) => (
                                <TableRow key={d.id} hover>
                                    <TableCell>{d.id}</TableCell>
                                    <TableCell>{d.name}</TableCell>
                                    <TableCell align="right">
                                        {d.labsCount}
                                    </TableCell>
                                    <TableCell className={styles.labWorksCell}>
                                        {d.labWorks
                                            .map((lw) => lw.name)
                                            .join(', ') || '—'}
                                    </TableCell>
                                    <TableCell align="right">
                                        <IconButton
                                            size="small"
                                            onClick={() => handleEdit(d)}
                                            aria-label={`Редактировать ${d.name}`}
                                        >
                                            <EditIcon fontSize="small" />
                                        </IconButton>
                                        <IconButton
                                            size="small"
                                            onClick={() => handleDeleteClick(d)}
                                            aria-label={`Удалить ${d.name}`}
                                        >
                                            <DeleteIcon fontSize="small" />
                                        </IconButton>
                                    </TableCell>
                                </TableRow>
                            ))}
                            {!loading && disciplines.length === 0 && (
                                <TableRow>
                                    <TableCell
                                        colSpan={5}
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

            <DisciplineDialog
                open={dialogOpen}
                discipline={editingDiscipline}
                onClose={() => setDialogOpen(false)}
                onSaved={handleSaved}
            />

            <ConfirmDialog
                open={deleteTarget !== null}
                title="Удалить Discipline?"
                message={`Удалить "${deleteTarget?.name}"? Это действие необратимо.`}
                onConfirm={handleConfirmDelete}
                onCancel={() => setDeleteTarget(null)}
            />
        </div>
    );
}

export default DisciplinesPage;
