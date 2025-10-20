package controller;

import dao.ClienteDAO;
import dao.PedidoDAO;
import dao.ProdutoDAO;
import dao.NotaFiscalDAO;
import model.Cliente;
import model.Pedido;
import model.Produto;
import model.NotaFiscal;
import view.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/PedidoServlet")
public class PedidoServlet extends HttpServlet {

    private PedidoDAO pedidoDAO;
    private ClienteDAO clienteDAO;
    private ProdutoDAO produtoDAO;
    private NotaFiscalDAO notaFiscalDAO;

    @Override
    public void init() throws ServletException {
        pedidoDAO = new PedidoDAO();
        clienteDAO = new ClienteDAO();
        produtoDAO = new ProdutoDAO();
        notaFiscalDAO = new NotaFiscalDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    request.setAttribute("clientes", clienteDAO.getAll());
                    request.setAttribute("produtos", produtoDAO.getAll());
                    View.render(request, response, "/pedidoForm.jsp");
                    break;

                case "edit": {
                    int idEdit = safeParseInt(request.getParameter("id"), 0);
                    if (idEdit > 0) {
                        Pedido pedidoEdit = pedidoDAO.getById(idEdit);
                        if (pedidoEdit != null) {
                            request.setAttribute("pedido", pedidoEdit);
                            request.setAttribute("clientes", clienteDAO.getAll());
                            request.setAttribute("produtos", produtoDAO.getAll());
                            View.render(request, response, "/pedidoForm.jsp");
                        } else {
                            View.redirect(response, "PedidoServlet?action=list");
                        }
                    } else {
                        View.redirect(response, "PedidoServlet?action=list");
                    }
                } break;

                case "delete": {
                    int idDelete = safeParseInt(request.getParameter("id"), 0);
                    if (idDelete > 0) {
                        pedidoDAO.delete(idDelete);
                    }
                    View.redirect(response, "PedidoServlet?action=list");
                } break;

                case "list":
                default:
                    List<Pedido> pedidos = pedidoDAO.getAll();
                    request.setAttribute("pedidos", pedidos);
                    View.render(request, response, "/pedidoList.jsp");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setCharacterEncoding("UTF-8");
            Pedido pedido = buildPedidoFromRequest(request);

            boolean novo = pedido.getId() == 0;

            if (novo) {
                pedidoDAO.insert(pedido);

                NotaFiscal nf = new NotaFiscal();
                nf.setPedidoId(pedido.getId());
                nf.setNumero("NF-" + pedido.getId() + "-" + System.currentTimeMillis());
                nf.setDataEmissao(new java.util.Date());
                nf.setValorTotal(pedido.getTotal());
                notaFiscalDAO.inserir(nf);

            } else {
                pedidoDAO.update(pedido);
            }

            View.redirect(response, "PedidoServlet?action=list");

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private Pedido buildPedidoFromRequest(HttpServletRequest request) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(safeParseInt(request.getParameter("id"), 0));

        Cliente cliente = clienteDAO.getById(safeParseInt(request.getParameter("clienteId"), 0));
        pedido.setCliente(cliente);

        List<Produto> produtosSelecionados = new ArrayList<>();
        String[] produtosIds = request.getParameterValues("produtosId");
        if (produtosIds != null) {
            for (String pid : produtosIds) {
                Produto pr = produtoDAO.getById(safeParseInt(pid, 0));
                if (pr != null) produtosSelecionados.add(pr);
            }
        }
        pedido.setProdutos(produtosSelecionados);

        pedido.setStatus(request.getParameter("status"));
        pedido.setFormaPagamento(request.getParameter("formaPagamento")); // Se esse campo existir mesmo
        pedido.setObservacoes(request.getParameter("observacoes"));      // ** ADICIONADO **
        pedido.setCodigoPedido(request.getParameter("codigoPedido"));    // ** ADICIONADO **
        pedido.setEntregue("on".equals(request.getParameter("entregue")));
        pedido.setDataPedido(LocalDate.now());

        return pedido;
    }

    private int safeParseInt(String value, int defaultVal) {
        try { return Integer.parseInt(value); } catch (Exception e) { return defaultVal; }
    }
}
