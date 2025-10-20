package controller;

import dao.ClienteDAO;
import model.Cliente;
import view.View;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {

    private ClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        clienteDAO = new ClienteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    View.render(request, response, "/clienteForm.jsp");
                    break;

                case "edit": {
                    String idStr = request.getParameter("id");
                    if (idStr != null && !idStr.isEmpty()) {
                        try {
                            int idEdit = Integer.parseInt(idStr);
                            Cliente clienteEdit = clienteDAO.getById(idEdit);
                            if (clienteEdit != null) {
                                request.setAttribute("cliente", clienteEdit);
                                View.render(request, response, "/clienteForm.jsp");
                            } else {
                                View.redirect(response, "ClienteServlet?action=list");
                            }
                        } catch (NumberFormatException e) {
                            View.redirect(response, "ClienteServlet?action=list");
                        }
                    } else {
                        View.redirect(response, "ClienteServlet?action=list");
                    }
                } break;

                case "delete": {
                    String idStr = request.getParameter("id");
                    if (idStr != null && !idStr.isEmpty()) {
                        try {
                            int idDelete = Integer.parseInt(idStr);
                            clienteDAO.delete(idDelete);
                        } catch (NumberFormatException e) {
                        }
                    }
                    View.redirect(response, "ClienteServlet?action=list");
                } break;

                case "list":
                default:
                    List<Cliente> clientes = clienteDAO.getAll();
                    request.setAttribute("clientes", clientes);
                    View.render(request, response, "/clienteList.jsp");
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

            String idStr = request.getParameter("id");
            Cliente cliente = new Cliente();

            if (idStr != null && !idStr.isEmpty()) {
                try {
                    cliente.setId(Integer.parseInt(idStr));
                } catch (NumberFormatException e) {
                    cliente.setId(0);
                }
            }

            cliente.setNome(request.getParameter("nome"));
            cliente.setCpf(request.getParameter("cpf"));
            cliente.setEmail(request.getParameter("email"));
            cliente.setTelefone(request.getParameter("telefone"));
            cliente.setEndereco(request.getParameter("endereco"));

            String dataNasc = request.getParameter("dataNascimento");
            if (dataNasc != null && !dataNasc.isEmpty()) {
                try {
                    cliente.setDataNascimento(LocalDate.parse(dataNasc));
                } catch (DateTimeParseException ex) {
                    cliente.setDataNascimento(null); // ou tratar conforme política da aplicação
                }
            }

            cliente.setCidade(request.getParameter("cidade"));
            cliente.setEstado(request.getParameter("estado"));
            cliente.setCep(request.getParameter("cep"));

            if (cliente.getId() > 0) {
                clienteDAO.update(cliente);
            } else {
                clienteDAO.insert(cliente);
            }

            View.redirect(response, "ClienteServlet?action=list");

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
