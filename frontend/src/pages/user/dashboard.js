import '../../assets/styles/dashboard.css';
import { useState, useRef } from 'react';
import DashboardDetailBox from '../../components/userDashboard/dashboardDetailBox';
import AuthService from '../../services/auth.service';
import CategoryExpenseChart from '../../components/userDashboard/categoryExpenseChart';
import Header from '../../components/utils/header';
import Budget from '../../components/userDashboard/budget';
import useDashboard from '../../hooks/useDashboard';
import Loading from '../../components/utils/loading';
import Info from '../../components/utils/Info';
import Container from '../../components/utils/Container';
import toast, { Toaster } from 'react-hot-toast';
import { jsPDF } from 'jspdf';
import 'jspdf-autotable';
import html2canvas from 'html2canvas';
import UserService from '../../services/userService';

function Dashboard() {
    const months = getMonths();
    const [currentMonth, setMonth] = useState(months[0]);
    const chartRef = useRef();

    const [total_expense, total_income, cash_in_hand, no_of_transactions, categorySummary, budgetAmount, saveBudget, isLoading, isError] = useDashboard(currentMonth);
    const [transactionHistory, setTransactionHistory] = useState([]);

    const onMonthChange = (id) => {
        const month = months.find(m => m.id == id);
        setMonth(month);
    };

    const fetchTransactionHistory = async (email, month) => {
        try {
            const response = await UserService.getMonthlyTransactionsByUser(email, month);
            if (response.data.status === 'SUCCESS') {
                const transactions = response.data.response;
                setTransactionHistory(transactions);
                return transactions;
            }
        } catch (error) {
            toast.error("Failed to fetch transaction history.");
            return [];
        }
    };

    const generateReport = async () => {
        const transactions = await fetchTransactionHistory(AuthService.getCurrentUser().email, currentMonth.id);
        const doc = new jsPDF();

        doc.setFont("helvetica", "bold");
        doc.setFontSize(22);
        doc.setTextColor(0, 0, 0);
        doc.text(`Financial Report - ${currentMonth.monthName} ${currentMonth.year}`, 10, 20);

        const currentTime = new Date().toLocaleTimeString();
        const currentDate = new Date().toLocaleDateString('en-GB');
        doc.setFont("helvetica", "normal");
        doc.setFontSize(12);
        doc.setTextColor(0, 0, 0);
        doc.text(`Generated on: ${currentDate} at ${currentTime}`, 10, 30);

        doc.setDrawColor(0);
        doc.setLineWidth(0.5);
        doc.line(10, 35, 200, 35);

        doc.setFontSize(16);
        doc.setTextColor(0, 102, 204);
        doc.text("Dashboard Overview", 10, 45);

        doc.setFontSize(12);
        doc.setTextColor(0, 0, 0);
        const overviewData = [
            { label: "Total Income", value: `${total_income}` },
            { label: "Total Expense", value: `${total_expense}` },
            { label: "Cash in Hand", value: `${cash_in_hand}` },
            { label: "Number of Transactions", value: `${no_of_transactions}` },
        ];

        let yOffset = 55;
        overviewData.forEach((item) => {
            doc.text(`${item.label}: ${item.value}`, 10, yOffset);
            yOffset += 10;
        });

        doc.line(10, yOffset + 5, 200, yOffset + 5);

        yOffset += 15;
        doc.setFontSize(16);
        doc.setTextColor(0, 102, 204);
        doc.text("Budget Overview", 10, yOffset);
        doc.setFontSize(14);
        doc.setTextColor(0, 0, 0);
        const remainingBudget = budgetAmount - total_expense;
        const budgetData = [
            { label: "Budget Amount", value: `${budgetAmount}` },
            { label: "Remaining Budget", value: `${remainingBudget}` },
        ];

        yOffset += 10;
        budgetData.forEach((item) => {
            doc.text(`${item.label}: ${item.value}`, 10, yOffset);
            yOffset += 10;
        });

        doc.line(10, yOffset + 5, 200, yOffset + 5);

        try {
            const canvas = await html2canvas(chartRef.current);
            const imgData = canvas.toDataURL('image/png');
            const imgWidth = 240;
            const imgHeight = (canvas.height * imgWidth) / canvas.width;
            const x = (doc.internal.pageSize.getWidth() - imgWidth) / 2;
            const chartYOffset = yOffset + 30;

            doc.setFontSize(16);
            doc.setTextColor(0, 102, 204);
            doc.text("Category Expense Chart", 10, chartYOffset - 10);

            doc.addImage(imgData, 'PNG', x, chartYOffset, imgWidth, imgHeight);
            yOffset = chartYOffset + imgHeight + 10;
        } catch (error) {
            console.error("Error generating chart image", error);
        }

        doc.addPage();
        const headingText = `Transaction History - ${currentMonth.monthName} ${currentMonth.year}`;

        doc.setFontSize(22);
        doc.setFont("helvetica", "bold");
        doc.setTextColor(0, 0, 0);
        doc.text(headingText, 10, 20);

        const columns = ["Date", "Description", "Category", "Amount"];
        const rows = transactions.map(transaction => {
            return [
                new Date(transaction.date).toLocaleDateString('en-GB'),
                transaction.description,
                transaction.categoryName,
                transaction.amount.toString()
            ];
        });

        doc.autoTable({
            startY: 40,
            head: [columns],
            body: rows,
            theme: 'grid',
            margin: { left: 10, right: 10 },
            styles: {
                fontSize: 12,
                cellPadding: 4,
                halign: 'center',
                valign: 'middle',
            },
            headStyles: {
                fillColor: [0, 102, 204],
                textColor: [255, 255, 255],
                fontStyle: 'bold',
            },
            alternateRowStyles: {
                fillColor: [240, 240, 240],
            },
            columnStyles: {
                0: { cellWidth: 35 },
                1: { cellWidth: 70 },
                2: { cellWidth: 50 },
                3: { cellWidth: 35 },
            },
        });

        doc.save(`${currentMonth.monthName}-${currentMonth.year}-financial-report.pdf`);
        toast.success(`Report for ${currentMonth.monthName} ${currentMonth.year} has been generated!`);
    };


    return (
        <Container activeNavId={0}>
            <Header title="Dashboard" />
            <Toaster />
            {isLoading && <Loading />}
            {isError && toast.error("Failed to fetch information. Try again later!")}
            {!isError &&
                <div className="month-selector-container">
                    <SelectMonth months={months} onMonthChange={onMonthChange} />
                    <button
                        className="generate-report-button"
                        onClick={generateReport}>
                        Generate Report
                    </button>
                </div>
            }
            {!isLoading && total_expense === 0 && <Info text="You have no expenses in this month!" />}
            {!isError && total_expense !== 0 &&
                <>
                    <DashboardDetailBox total_expense={total_expense} total_income={total_income} cash_in_hand={cash_in_hand} no_of_transactions={no_of_transactions} />
                    <div className="dashboard-chart" ref={chartRef}>
                        <CategoryExpenseChart categorySummary={categorySummary} />
                        <Budget totalExpense={total_expense} budgetAmount={budgetAmount} saveBudget={saveBudget} currentMonth={currentMonth} />
                    </div>
                </>
            }
        </Container>
    );
}

export default Dashboard;

function getMonths() {
    const months = [];
    const current_date = new Date();
    for (let i = 0; i <= 23; i++) {
        const date = new Date(current_date.getFullYear(), current_date.getMonth() - i, 1);
        months.push({ id: date.getMonth() + 1, year: date.getFullYear(), monthName: date.toLocaleString('en-US', { month: 'long' }) });
    }
    return months;
}

function SelectMonth({ months, onMonthChange }) {
    return (
        <div className="select-month-container">
            <select onChange={(e) => onMonthChange(e.target.value)}>
                {months.map((m) => <option value={m.id} key={m.id}>{m.monthName} {m.year}</option>)}
            </select>
        </div>
    );
}