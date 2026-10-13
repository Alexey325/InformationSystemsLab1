import { useEffect, useState } from 'react';
import {
    Paper,
    Typography,
    TextField,
    Button,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    TableSortLabel,
    TablePagination,
    IconButton,
    Alert,
    CircularProgress,
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { deleteLabWork, fetchLabWorks } from '../../api/labworks';
import type { LabWork } from '../../types/labwork';
import type { Page } from '../../types/api';
import ConfirmDialog from '../../components/ConfirmDialog';
import LabWorkDialog from './LabWorkDialog';
import styles from './LabWorksPage.module.css';

type SortField = 'name' | 'description' | null;
type SortDir = 'asc' | 'desc';

const EMPTY_PAGE: Page<LabWork> = {
    content: [],
    totalElements: 0,
    totalPages: 0,
    number: 0,
    size: 10,
    first: true,
    last: true,
    empty: true,
};

function LabWorksPage() {
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [sortField, setSortField] = useState<SortField>(null);
    const [sortDir, setSortDir] = useState<SortDir>('asc');
    const [nameFilter, setNameFilter] = useState('');
    const [descriptionFilter, setDescriptionFilter] = useState('');

    const [data, setData] = useState<Page<LabWork>>(EMPTY_PAGE);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [refreshKey, setRefreshKey] = useState(0);

    const [dialogOpen, setDialogOpen] = useState(false);
    const [editingLabWork, setEditingLabWork] = useState<LabWork | null>(null);
    const [deleteTarget, setDeleteTarget] = useState<LabWork | null>(null);

    useEffect(() => {
        setLoading(true);
        setError(null);
        fetchLabWorks({
            page,
            size: rowsPerPage,
            sortField,
            sortDir,
            name: nameFilter,
            description: descriptionFilter,
        })
            .then(setData)
            .catch((err: Error) => setError(err.message))
            .finally(() => setLoading(false));
    }, [
        page,
        rowsPerPage,
        sortField,
        sortDir,
        nameFilter,
        descriptionFilter,
        refreshKey,
    ]);

    function handleAdd() {
        setEditingLabWork(null);
        setDialogOpen(true);
    }

    function handleEdit(lw: LabWork) {
        setEditingLabWork(lw);
        setDialogOpen(true);
    }

    function handleSaved() {
        setRefreshKey((k) => k + 1);
    }

    function handleDeleteClick(lw: LabWork) {
        setDeleteTarget(lw);
    }

    function handleConfirmDelete() {
        if (!deleteTarget) return;
        deleteLabWork(deleteTarget.id)
            .then(() => setRefreshKey((k) => k + 1))
            .catch((err: Error) => setError(err.message))
            .finally(() => setDeleteTarget(null));
    }

    function handleSort(field: SortField) {
        if (sortField !== field) {
            setSortField(field);
            setSortDir('asc');
        } else if (sortDir === 'asc') {
            setSortDir('desc');
        } else {
            setSortField(null);
        }
        setPage(0);
    }

    return (
        <div className={styles.page}>
            <Paper className={styles.headerCard}>
                <Typography variant="h5" className={styles.title}>
                    LabWork
                </Typography>
                <span className={styles.totalBadge}>
                    Всего: {data.totalElements}
                </span>
            </Paper>

            <Paper className={styles.filtersCard}>
                <div className={styles.filtersGroup}>
                    <TextField
                        label="Имя"
                        className={styles.filterField}
                        value={nameFilter}
                        onChange={(e) => {
                            setPage(0);
                            setNameFilter(e.target.value);
                        }}
                    />
                    <TextField
                        label="Описание"
                        className={styles.filterField}
                        value={descriptionFilter}
                        onChange={(e) => {
                            setPage(0);
                            setDescriptionFilter(e.target.value);
                        }}
                    />
                </div>
                <Button variant="contained" onClick={handleAdd}>
                    + Добавить LabWork
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
                                    <TableSortLabel
                                        active={sortField === 'name'}
                                        direction={
                                            sortField === 'name'
                                                ? sortDir
                                                : 'asc'
                                        }
                                        onClick={() => handleSort('name')}
                                    >
                                        Name
                                    </TableSortLabel>
                                </TableCell>
                                <TableCell className={styles.headCell}>
                                    Coordinates
                                </TableCell>
                                <TableCell className={styles.headCell}>
                                    Creation date
                                </TableCell>
                                <TableCell className={styles.headCell}>
                                    <TableSortLabel
                                        active={sortField === 'description'}
                                        direction={
                                            sortField === 'description'
                                                ? sortDir
                                                : 'asc'
                                        }
                                        onClick={() =>
                                            handleSort('description')
                                        }
                                    >
                                        Description
                                    </TableSortLabel>
                                </TableCell>
                                <TableCell className={styles.headCell}>
                                    Difficulty
                                </TableCell>
                                <TableCell className={styles.headCell}>
                                    Discipline
                                </TableCell>
                                <TableCell
                                    className={styles.headCell}
                                    align="right"
                                >
                                    Min. point
                                </TableCell>
                                <TableCell
                                    className={styles.headCell}
                                    align="right"
                                >
                                    Pers. qual. max
                                </TableCell>
                                <TableCell
                                    className={styles.headCell}
                                    align="right"
                                >
                                    Tuned in
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
                            {data.content.map((lw) => (
                                <TableRow key={lw.id} hover>
                                    <TableCell>{lw.id}</TableCell>
                                    <TableCell>{lw.name}</TableCell>
                                    <TableCell>
                                        {lw.coordinates.x}, {lw.coordinates.y}
                                    </TableCell>
                                    <TableCell>{lw.creationDate}</TableCell>
                                    <TableCell>{lw.description}</TableCell>
                                    <TableCell>
                                        {lw.difficulty ?? '—'}
                                    </TableCell>
                                    <TableCell>{lw.discipline?.name}</TableCell>
                                    <TableCell align="right">
                                        {lw.minimalPoint}
                                    </TableCell>
                                    <TableCell align="right">
                                        {lw.personalQualitiesMaximum}
                                    </TableCell>
                                    <TableCell align="right">
                                        {lw.tunedInWork}
                                    </TableCell>
                                    <TableCell align="right">
                                        <IconButton
                                            size="small"
                                            onClick={() => handleEdit(lw)}
                                            aria-label={`Редактировать ${lw.name}`}
                                        >
                                            <EditIcon fontSize="small" />
                                        </IconButton>
                                        <IconButton
                                            size="small"
                                            onClick={() =>
                                                handleDeleteClick(lw)
                                            }
                                            aria-label={`Удалить ${lw.name}`}
                                        >
                                            <DeleteIcon fontSize="small" />
                                        </IconButton>
                                    </TableCell>
                                </TableRow>
                            ))}
                            {!loading && data.content.length === 0 && (
                                <TableRow>
                                    <TableCell
                                        colSpan={10}
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

                <TablePagination
                    component="div"
                    count={data.totalElements}
                    page={page}
                    onPageChange={(_, newPage) => setPage(newPage)}
                    rowsPerPage={rowsPerPage}
                    onRowsPerPageChange={(e) => {
                        setRowsPerPage(parseInt(e.target.value, 10));
                        setPage(0);
                    }}
                    rowsPerPageOptions={[5, 10, 20, 50]}
                    labelRowsPerPage="Показывать по:"
                    labelDisplayedRows={({ from, to, count }) =>
                        `${from}–${to} из ${count}`
                    }
                />
            </Paper>

            <LabWorkDialog
                open={dialogOpen}
                labWork={editingLabWork}
                onClose={() => setDialogOpen(false)}
                onSaved={handleSaved}
            />

            <ConfirmDialog
                open={deleteTarget !== null}
                title="Удалить LabWork?"
                message={`Удалить "${deleteTarget?.name}"? Это действие необратимо.`}
                onConfirm={handleConfirmDelete}
                onCancel={() => setDeleteTarget(null)}
            />
        </div>
    );
}

export default LabWorksPage;
